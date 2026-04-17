package com.demo.demo;

import com.demo.demo.dto.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class LoginController {

    private final LoginService loginService;

    // Helper class to store policy and relation together
    private static class PolicyRelation {
        String policyNo;
        PolicyClientRelationshipDto relation;
        
        PolicyRelation(String policyNo, PolicyClientRelationshipDto relation) {
            this.policyNo = policyNo;
            this.relation = relation;
        }
    }
    
    // Helper class to store aggregated nominee and appointee data
    private static class NomineeAppointeeData {
        Set<String> nomineeClientIds;
        Map<String, PolicyRelation> nomineeMap;
        Set<String> appointeeClientIds;
        Map<String, PolicyRelation> appointeeMap;
        
        NomineeAppointeeData(Set<String> nomineeClientIds, Map<String, PolicyRelation> nomineeMap,
                            Set<String> appointeeClientIds, Map<String, PolicyRelation> appointeeMap) {
            this.nomineeClientIds = nomineeClientIds;
            this.nomineeMap = nomineeMap;
            this.appointeeClientIds = appointeeClientIds;
            this.appointeeMap = appointeeMap;
        }
    }
    
    // Helper class to store riders and policies
    private static class RidersAndPolicies {
        List<RiderDto> riders;
        List<PolicyResponseDto> policies;
        
        RidersAndPolicies(List<RiderDto> riders, List<PolicyResponseDto> policies) {
            this.riders = riders;
            this.policies = policies;
        }
    }
    
    // Helper class to store matched claimant information
    private static class MatchedClaimant {
        String clientId;
        String clientType; // "Nominee" or "Appointee"
        
        MatchedClaimant(String clientId, String clientType) {
            this.clientId = clientId;
            this.clientType = clientType;
        }
    }

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @GetMapping("/api/login/{polId}")
    public LoginResponseDto hello(@PathVariable("polId") String polId) {
        String liDob = "1986-03-03";
        String claimantDob = "1965-09-03";
        // STEP 1: Fetch and validate main policy
        PolicyDto mainPolicy = loginService.getPolicyDetails(polId);
        loginService.validatePolicy(mainPolicy);
        
        // STEP 2: Extract Life Insured client ID
        String liClientId = extractLifeInsuredClientId(mainPolicy);
        
        // STEP 3: Fetch Life Insured details
        ClientDto liDetails = loginService.getClientDetails(liClientId);
        
        // Validate Life Insured DOB
        String actualLiDob = null;
        if (liDetails.getResponse() != null && 
            liDetails.getResponse().getPayload() != null && 
            liDetails.getResponse().getPayload().getPersonalDetail() != null) {
            actualLiDob = liDetails.getResponse().getPayload().getPersonalDetail().getDob();
        }
        
        if (actualLiDob == null || !liDob.equals(actualLiDob)) {
            throw new RuntimeException("Life Insured date of birth does not match");
        }
        
        // STEP 4: Fetch all policies for Life Insured
        List<PolicyDto> allPolicies = fetchAllPoliciesForLifeInsured(mainPolicy, liDetails);
        
        // STEP 5: Aggregate nominees and appointees across all policies
        NomineeAppointeeData nomineeAppointeeData = aggregateNomineesAndAppointees(allPolicies);
        
        // STEP 6: Validate counts
        validateNomineeAndAppointeeCount(nomineeAppointeeData);
        
        // Option 1
        // TODO: OTP send API call
        // TODO: OTP verify API call
        
        // STEP 7: Map nominee details (after OTP verification)
        List<NomineeDto> nominees = mapNomineeDetails(nomineeAppointeeData.nomineeClientIds, nomineeAppointeeData.nomineeMap);
        
        // STEP 8: Map appointee details (after OTP verification)
        List<AppointeeDto> appointees = mapAppointeeDetails(nomineeAppointeeData.appointeeClientIds, nomineeAppointeeData.appointeeMap);
        
        // STEP 8A: Validate claimant DOB and find matching client
        MatchedClaimant matchedClaimant = validateAndMatchClaimantDob(claimantDob, nominees, appointees);
        System.out.println("Matched Claimant - ID: " + matchedClaimant.clientId + ", Type: " + matchedClaimant.clientType);
        
        // Option 2
        // TODO: OTP send API call
        // TODO: OTP verify API call

        // STEP 9: Build riders and policies arrays
        RidersAndPolicies ridersAndPolicies = buildRidersAndPolicies(allPolicies, liDetails);
        
        // STEP 10: Build final response
        return buildFinalResponse(mainPolicy, liDetails, liClientId, nominees, appointees, ridersAndPolicies);
    }
    
    /**
     * Extract Life Insured client ID from policy
     * Life Insured is identified by relatnType = "I" in policyClientRelationship
     */
    private String extractLifeInsuredClientId(PolicyDto mainPolicy) {
        PolicyDto.Payload payload = mainPolicy.getResponse().getPayload();
        
        if (payload.getPolicyClientRelationship() != null) {
            for (PolicyClientRelationshipDto relation : payload.getPolicyClientRelationship()) {
                String relatnType = relation.getRelatnType();
                if (relatnType != null && "I".equals(relatnType.trim())) {
                    String clientId = relation.getClientID();
                    if (clientId != null) {
                        return clientId.trim();
                    }
                }
            }
        }
        
        throw new RuntimeException("Life Insured not found in policy");
    }
    
    /**
     * Fetch all policies associated with Life Insured
     * Policies are gathered from clientPolicyRelationship and payerdtls
     */
    private List<PolicyDto> fetchAllPoliciesForLifeInsured(PolicyDto mainPolicy, ClientDto liDetails) {
        ClientDto.Payload liPayload = liDetails.getResponse().getPayload();
        Set<String> policyIds = new HashSet<>();
        
        // Get policy IDs from clientPolicyRelationship
        if (liPayload.getClientPolicyRelationship() != null) {
            for (ClientPolicyRelationshipDto relation : liPayload.getClientPolicyRelationship()) {
                String policyId = relation.getPolId();
                if (policyId != null && !policyId.trim().isBlank()) {
                    policyIds.add(policyId.trim());
                }
            }
        }
        
        // Get policy IDs from payerdtls
        if (liPayload.getPayerdtls() != null) {
            for (PayerDetailDto payer : liPayload.getPayerdtls()) {
                String policyNo = payer.getPolicyNo();
                if (policyNo != null && !policyNo.trim().isBlank()) {
                    policyIds.add(policyNo.trim());
                }
            }
        }
        
        // Remove main policy ID (already fetched)
        String mainPolicyId = mainPolicy.getResponse().getPayload().getPolicyNo();
        policyIds.remove(mainPolicyId);
        
        System.out.println("All policy IDs: " + policyIds);
        
        // Fetch all policies
        List<PolicyDto> allPolicies = new ArrayList<>();
        allPolicies.add(mainPolicy);
        
        for (String pId : policyIds) {
            PolicyDto policy = loginService.getPolicyDetails(pId);
            loginService.validatePolicy(policy);
            allPolicies.add(policy);
        }
        
        return allPolicies;
    }
    
    /**
     * Aggregate nominees and appointees across all policies
     * Processes policies once to extract both nominees and appointees
     */
    private NomineeAppointeeData aggregateNomineesAndAppointees(List<PolicyDto> allPolicies) {
        Set<String> nomineeClientIds = new HashSet<>();
        Map<String, PolicyRelation> nomineeMap = new HashMap<>();
        Set<String> appointeeClientIds = new HashSet<>();
        Map<String, PolicyRelation> appointeeMap = new HashMap<>();
        List<String> excludeRelatnTypes = Arrays.asList("I");
        
        for (PolicyDto policy : allPolicies) {
            PolicyDto.Payload polPayload = policy.getResponse().getPayload();
            
            // Extract nominees from nomineeDetails
            extractNomineesFromPolicy(polPayload, nomineeClientIds, nomineeMap);
            
            // Extract appointees from policyClientRelationship
            extractAppointeesFromPolicy(polPayload, appointeeClientIds, appointeeMap, excludeRelatnTypes);
        }
        
        return new NomineeAppointeeData(nomineeClientIds, nomineeMap, appointeeClientIds, appointeeMap);
    }
    
    /**
     * Extract nominees from a single policy's nomineeDetails
     */
    private void extractNomineesFromPolicy(PolicyDto.Payload polPayload, 
                                          Set<String> nomineeClientIds, 
                                          Map<String, PolicyRelation> nomineeMap) {
        NomineeDetailsDto nomineeDetails = polPayload.getNomineeDetails();
        if (nomineeDetails != null && nomineeDetails.getNominees() != null) {
            for (NomineeDetailsDto.NomineeDto nominee : nomineeDetails.getNominees()) {
                String nomineeClientID = nominee.getNomineeClientID();
                if (nomineeClientID != null && !nomineeClientID.trim().isBlank()) {
                    String trimmedNomineeId = nomineeClientID.trim();
                    nomineeClientIds.add(trimmedNomineeId);
                    
                    // Create PolicyClientRelationshipDto to hold nominee data
                    PolicyClientRelationshipDto nomineeRelationDto = new PolicyClientRelationshipDto();
                    nomineeRelationDto.setRelatnType(nominee.getNomineeRelation());
                    nomineeRelationDto.setPrcntShareOfNom(nominee.getPercentageShare());
                    nomineeRelationDto.setClientID(nomineeClientID);
                    
                    nomineeMap.put(trimmedNomineeId, new PolicyRelation(polPayload.getPolicyNo(), nomineeRelationDto));
                }
            }
        }
    }
    
    /**
     * Extract appointees from a single policy's policyClientRelationship
     * Excludes Life Insured (relatnType = "I")
     */
    private void extractAppointeesFromPolicy(PolicyDto.Payload polPayload,
                                            Set<String> appointeeClientIds,
                                            Map<String, PolicyRelation> appointeeMap,
                                            List<String> excludeRelatnTypes) {
        if (polPayload.getPolicyClientRelationship() != null) {
            for (PolicyClientRelationshipDto relation : polPayload.getPolicyClientRelationship()) {
                String clientID = relation.getClientID();
                String relatnType = relation.getRelatnType();
                
                if (clientID != null && !clientID.trim().isBlank()) {
                    String trimmedRelatnType = relatnType != null ? relatnType.trim() : "";
                    
                    if (!excludeRelatnTypes.contains(trimmedRelatnType)) {
                        String trimmedClientId = clientID.trim();
                        appointeeClientIds.add(trimmedClientId);
                        appointeeMap.put(trimmedClientId, new PolicyRelation(polPayload.getPolicyNo(), relation));
                    }
                }
            }
        }
    }
    
    /**
     * Validate nominee and appointee counts
     * Business rules: At least 1 nominee required, max 1 nominee allowed, max 1 appointee allowed
     */
    private void validateNomineeAndAppointeeCount(NomineeAppointeeData data) {
        System.out.println("Nominee Client IDs: " + data.nomineeClientIds);
        if (data.nomineeClientIds.isEmpty()) {
            throw new RuntimeException("At least 1 Nominee is required");
        }
        
        if (data.nomineeClientIds.size() > 1) {
            throw new RuntimeException("Multiple nominees not allowed");
        }
        
        System.out.println("Appointee Client IDs: " + data.appointeeClientIds);
        if (data.appointeeClientIds.size() > 1) {
            throw new RuntimeException("Multiple appointees not allowed");
        }
    }
    
    /**
     * Map nominee details by calling getClientDetails for each nominee
     * Transforms client data into NomineeDto format
     */
    private List<NomineeDto> mapNomineeDetails(Set<String> nomineeClientIds, 
                                               Map<String, PolicyRelation> nomineeMap) {
        List<NomineeDto> allNomineeDetails = new ArrayList<>();
        
        for (String nomineeId : nomineeClientIds) {
            ClientDto nomineeDetail = loginService.getClientDetails(nomineeId);
            NomineeDto nomineeDto = buildNomineeDto(nomineeId, nomineeDetail, nomineeMap);
            allNomineeDetails.add(nomineeDto);
        }
        
        return allNomineeDetails;
    }
    
    /**
     * Build a single NomineeDto from client details
     */
    private NomineeDto buildNomineeDto(String nomineeId, ClientDto nomineeDetail, 
                                       Map<String, PolicyRelation> nomineeMap) {
        ClientDto.Payload nomineePayload = nomineeDetail.getResponse().getPayload();
        PolicyRelation policyRelation = nomineeMap.get(nomineeId);
        String associatedPolicyNo = policyRelation != null ? policyRelation.policyNo : null;
        PolicyClientRelationshipDto nomineeRelation = policyRelation != null ? policyRelation.relation : null;
        
        NomineeDto nomineeDto = new NomineeDto();
        nomineeDto.setPolicyNo(associatedPolicyNo);
        nomineeDto.setClientID(nomineeId);
        
        // Personal details from client360
        if (nomineePayload.getPersonalDetail() != null) {
            PersonalDetailDto personalDetail = nomineePayload.getPersonalDetail();
            nomineeDto.setLastNomineeChangeDate(personalDetail.getClientNameChangeDate());
            nomineeDto.setSalutation(personalDetail.getTitle());
            nomineeDto.setFirstName(personalDetail.getFirstName());
            nomineeDto.setLastName(personalDetail.getLastName());
            nomineeDto.setDob(personalDetail.getDob());
            nomineeDto.setNationality(personalDetail.getNationality());
            nomineeDto.setMaritalStatus(personalDetail.getMaritalStatus());
            nomineeDto.setGender(personalDetail.getGenderDesc());
        }
        
        nomineeDto.setAge("");
        
        if (nomineeRelation != null) {
            nomineeDto.setRelationshipWithInsured(nomineeRelation.getRelatnType());
            nomineeDto.setSharePerFloat(nomineeRelation.getPrcntShareOfNom());
        }
        
        // Address fields
        if (nomineePayload.getBaseAddDetail() != null && !nomineePayload.getBaseAddDetail().isEmpty()) {
            BaseAddDetailDto address = nomineePayload.getBaseAddDetail().get(0);
            nomineeDto.setHouseNoAptNameSociety(address.getAddrLine1());
            nomineeDto.setRoadAreaSector(address.getAddrLine2());
            nomineeDto.setLandmark(address.getAddrLine3());
            nomineeDto.setVillageTownCityDistrict(address.getCity());
            nomineeDto.setCountry(address.getCountry());
            nomineeDto.setStateUT(address.getState());
            nomineeDto.setPincode(address.getPinCode());
        }
        
        // Contact details
        if (nomineePayload.getContactDetail() != null && !nomineePayload.getContactDetail().isEmpty()) {
            ContactDetailDto contact = nomineePayload.getContactDetail().get(0);
            nomineeDto.setContactNumber(contact.getMobNum());
            nomineeDto.setEmail(contact.getEmalId());
        }
        
        nomineeDto.setAmountN("");
        
        // PAN and Aadhar
        if (nomineePayload.getIdentifiableInfo() != null) {
            IdentifiableInfoDto identInfo = nomineePayload.getIdentifiableInfo();
            nomineeDto.setPanNo(identInfo.getPanNum());
            nomineeDto.setAadharNo(identInfo.getAadhaarNum());
        }
        
        nomineeDto.setBeneficiaryType("nominee");
        
        // Empty fields
        nomineeDto.setNameAsPerNSDL("");
        nomineeDto.setStatus("");
        nomineeDto.setPanLinked("");
        nomineeDto.setMobileNoCust("");
        nomineeDto.setVoterIDCard("");
        nomineeDto.setPassport("");
        nomineeDto.setDeathBenefitOption("");
        nomineeDto.setAmlStatus("");
        nomineeDto.setNomineeCoreChk("");
        nomineeDto.setScreeningID("");
        nomineeDto.setStateUTCount("");
        nomineeDto.setNsdlCheck("");
        nomineeDto.setPanExist("");
        
        return nomineeDto;
    }
    
    /**
     * Map appointee details by calling getClientDetails for each appointee
     * Transforms client data into AppointeeDto format
     */
    private List<AppointeeDto> mapAppointeeDetails(Set<String> appointeeClientIds,
                                                   Map<String, PolicyRelation> appointeeMap) {
        List<AppointeeDto> allAppointeeDetails = new ArrayList<>();
        
        for (String appointeeId : appointeeClientIds) {
            ClientDto appointeeDetail = loginService.getClientDetails(appointeeId);
            AppointeeDto appointeeDto = buildAppointeeDto(appointeeId, appointeeDetail, appointeeMap);
            allAppointeeDetails.add(appointeeDto);
        }
        
        return allAppointeeDetails;
    }
    
    /**
     * Build a single AppointeeDto from client details
     */
    private AppointeeDto buildAppointeeDto(String appointeeId, ClientDto appointeeDetail,
                                          Map<String, PolicyRelation> appointeeMap) {
        ClientDto.Payload appointeePayload = appointeeDetail.getResponse().getPayload();
        PolicyRelation policyRelation = appointeeMap.get(appointeeId);
        String associatedPolicyNo = policyRelation != null ? policyRelation.policyNo : null;
        PolicyClientRelationshipDto appointeeRelation = policyRelation != null ? policyRelation.relation : null;
        
        AppointeeDto appointeeDto = new AppointeeDto();
        appointeeDto.setPolicyNo(associatedPolicyNo);
        appointeeDto.setClientID(appointeeId);
        
        // Personal details
        if (appointeePayload.getPersonalDetail() != null) {
            PersonalDetailDto personalDetail = appointeePayload.getPersonalDetail();
            appointeeDto.setSalutation(personalDetail.getTitle());
            appointeeDto.setFirstName(personalDetail.getFirstName());
            appointeeDto.setLastName(personalDetail.getLastName());
            appointeeDto.setDob(personalDetail.getDob());
            appointeeDto.setGender(personalDetail.getGenderDesc());
            appointeeDto.setNationality(personalDetail.getNationality());
            appointeeDto.setMaritalStatus(personalDetail.getMaritalStatus());
        }
        
        appointeeDto.setAge("");
        
        if (appointeeRelation != null) {
            appointeeDto.setRelnWithProposer(appointeeRelation.getRelatnType());
            appointeeDto.setPercentProceed(appointeeRelation.getPrcntShareOfNom());
        }
        
        appointeeDto.setRelnWithNominee("");
        
        // Address fields
        if (appointeePayload.getBaseAddDetail() != null && !appointeePayload.getBaseAddDetail().isEmpty()) {
            BaseAddDetailDto address = appointeePayload.getBaseAddDetail().get(0);
            appointeeDto.setHouseno(address.getAddrLine1());
            appointeeDto.setRoadarea(address.getAddrLine2());
            appointeeDto.setLandmark(address.getAddrLine3());
            appointeeDto.setVillage(address.getCity());
            appointeeDto.setCountry(address.getCountry());
            appointeeDto.setState(address.getState());
            appointeeDto.setZipCode(address.getPinCode());
        }
        
        // Contact details
        if (appointeePayload.getContactDetail() != null && !appointeePayload.getContactDetail().isEmpty()) {
            ContactDetailDto contact = appointeePayload.getContactDetail().get(0);
            appointeeDto.setAppContactNo(contact.getMobNum());
            appointeeDto.setEmail(contact.getEmalId());
        }
        
        appointeeDto.setAmount("");
        
        // PAN and Aadhar
        if (appointeePayload.getIdentifiableInfo() != null) {
            IdentifiableInfoDto identInfo = appointeePayload.getIdentifiableInfo();
            appointeeDto.setPan(identInfo.getPanNum());
            appointeeDto.setAadharNo(identInfo.getAadhaarNum());
        }
        
        // Empty fields
        appointeeDto.setNameAsPerNSDL("");
        appointeeDto.setStatus("");
        appointeeDto.setPanLink("");
        appointeeDto.setMobileNoCust("");
        appointeeDto.setVoterId("");
        appointeeDto.setPassport("");
        appointeeDto.setAppointeeCoreChk("");
        appointeeDto.setAmlStatus("");
        appointeeDto.setScreeningID("");
        
        return appointeeDto;
    }
    
    /**     * Validate claimant DOB against all nominees and appointees
     * Returns matched client ID and type if found
     */
    private MatchedClaimant validateAndMatchClaimantDob(String claimantDob, 
                                                        List<NomineeDto> nominees, 
                                                        List<AppointeeDto> appointees) {
        // Check nominees first
        for (NomineeDto nominee : nominees) {
            if (nominee.getDob() != null && claimantDob.equals(nominee.getDob())) {
                return new MatchedClaimant(nominee.getClientID(), "Nominee");
            }
        }
        
        // Check appointees
        for (AppointeeDto appointee : appointees) {
            if (appointee.getDob() != null && claimantDob.equals(appointee.getDob())) {
                return new MatchedClaimant(appointee.getClientID(), "Appointee");
            }
        }
        
        // No match found
        throw new RuntimeException("Claimant date of birth does not match any nominee or appointee");
    }
    
    /**     * Build riders and policies arrays from all policies
     * Processes policies once to extract both riders and policy details
     */
    private RidersAndPolicies buildRidersAndPolicies(List<PolicyDto> allPolicies, ClientDto liDetails) {
        ClientDto.Payload liPayload = liDetails.getResponse().getPayload();
        List<RiderDto> allRiders = new ArrayList<>();
        List<PolicyResponseDto> policyResponses = new ArrayList<>();
        
        for (PolicyDto policy : allPolicies) {
            PolicyDto.Payload polPayload = policy.getResponse().getPayload();
            
            // Find matching clientPolicyRelationship for this policy
            ClientPolicyRelationshipDto matchingRelation = findMatchingRelation(polPayload.getPolicyNo(), liPayload);
            
            // Build riders from coverageDetails
            buildRidersForPolicy(polPayload, allRiders);
            
            // Build policy response
            PolicyResponseDto policyResponse = buildPolicyResponse(polPayload, matchingRelation, liDetails);
            policyResponses.add(policyResponse);
        }
        
        return new RidersAndPolicies(allRiders, policyResponses);
    }
    
    /**
     * Find matching clientPolicyRelationship for a policy
     */
    private ClientPolicyRelationshipDto findMatchingRelation(String policyNo, ClientDto.Payload liPayload) {
        if (liPayload.getClientPolicyRelationship() != null) {
            for (ClientPolicyRelationshipDto relation : liPayload.getClientPolicyRelationship()) {
                if (policyNo.equals(relation.getPolId())) {
                    return relation;
                }
            }
        }
        return null;
    }
    
    /**
     * Build riders from a single policy's coverageDetails
     */
    private void buildRidersForPolicy(PolicyDto.Payload polPayload, List<RiderDto> allRiders) {
        if (polPayload.getCoverageDetails() != null) {
            for (CoverageDetailsDto coverage : polPayload.getCoverageDetails()) {
                RiderDto rider = new RiderDto();
                
                rider.setPolicyNo(polPayload.getPolicyNo());
                rider.setProductName(coverage.getCvgPlanIdDesc());
                rider.setPlanCode(coverage.getCvgPlanIdCd());
                rider.setCoverageStatus("");
                rider.setTerm(coverage.getRiderTerm());
                
                if (polPayload.getPolicyBasicDetails() != null) {
                    rider.setPremiumTerm(polPayload.getPolicyBasicDetails().getPolicyPmtTerm());
                    rider.setPolicyIssueDate(polPayload.getPolicyBasicDetails().getPolicyIssueDt());
                    rider.setPremiumMode(polPayload.getPolicyBasicDetails().getBillingFreqDesc());
                    rider.setPaidToDate(polPayload.getPolicyBasicDetails().getPolicyPtToDtNum());
                }
                
                rider.setMaturityExpiryDate(coverage.getCvgMatXpryDt());
                rider.setSumAssured(coverage.getCvgFaceAmt());
                rider.setAdmissibleNonAdmissible("");
                rider.setCoverageNo(coverage.getCvgNum());
                rider.setCoverageEffDt(coverage.getCvgIssEffDt());
                
                allRiders.add(rider);
            }
        }
    }
    
    /**
     * Build a single PolicyResponseDto
     */
    private PolicyResponseDto buildPolicyResponse(PolicyDto.Payload polPayload,
                                                  ClientPolicyRelationshipDto matchingRelation,
                                                  ClientDto liDetails) {
        ClientDto.Payload liClientPayload = liDetails.getResponse().getPayload();
        PolicyResponseDto policyResponse = new PolicyResponseDto();
        
        policyResponse.setPolicyNo(polPayload.getPolicyNo());
        
        if (matchingRelation != null) {
            policyResponse.setClientID(matchingRelation.getCliId());
            policyResponse.setCustRole(matchingRelation.getRelationship());
            policyResponse.setPolicyStatus(matchingRelation.getPolicyStatusDesc());
        }
        
        // Life Insured personal details
        if (liClientPayload.getPersonalDetail() != null) {
            PersonalDetailDto personalDetail = liClientPayload.getPersonalDetail();
            policyResponse.setLadob(personalDetail.getDob());
            policyResponse.setSalutation(personalDetail.getTitle());
            policyResponse.setInsuredFirstName(personalDetail.getFirstName());
            policyResponse.setInsuredLastName(personalDetail.getLastName());
            policyResponse.setGender(personalDetail.getGenderDesc());
            policyResponse.setAtrFlag(personalDetail.getAtrFlag());
        }
        
        // Policy basic details
        if (polPayload.getPolicyBasicDetails() != null) {
            policyResponse.setProductName(polPayload.getPolicyBasicDetails().getPolicyBasePlanIdDesc());
            policyResponse.setProductCode(polPayload.getPolicyBasicDetails().getPolicyBasePlanIdCd());
            policyResponse.setPolicyIssueDate(polPayload.getPolicyBasicDetails().getPolicyIssueDt());
            policyResponse.setPaidtoDate(polPayload.getPolicyBasicDetails().getPolicyPtToDtNum());
            policyResponse.setPremiumMode(polPayload.getPolicyBasicDetails().getBillingFreqDesc());
        }
        
        // Contact details
        if (liClientPayload.getContactDetail() != null && !liClientPayload.getContactDetail().isEmpty()) {
            policyResponse.setMobileNo(liClientPayload.getContactDetail().get(0).getMobNum());
        }
        
        policyResponse.setHniFlag("");
        policyResponse.setLegalFlag("");
        policyResponse.setPolicyDuration("");
        policyResponse.setGracePeriod("");
        
        return policyResponse;
    }
    
    /**
     * Build final LoginResponseDto with all aggregated data
     */
    private LoginResponseDto buildFinalResponse(PolicyDto mainPolicy, ClientDto liDetails, String liClientId,
                                               List<NomineeDto> nominees, List<AppointeeDto> appointees,
                                               RidersAndPolicies ridersAndPolicies) {
        LoginResponseDto response = new LoginResponseDto();
        
        // Set nominees and appointees
        response.setNominees(nominees);
        response.setAppointee(appointees);
        
        // Set riders and policies
        response.setRiders(ridersAndPolicies.riders);
        response.setPolicies(ridersAndPolicies.policies);
        
        // Populate PolicyDetails with Life Insured details
        PolicyDto.Payload mainPayload = mainPolicy.getResponse().getPayload();
        ClientDto.Payload liClientPayload = liDetails.getResponse().getPayload();
        
        LoginResponseDto.PolicyDetails policyDetails = new LoginResponseDto.PolicyDetails();
        
        policyDetails.setPolicyNo(mainPayload.getPolicyNo());
        policyDetails.setClientID(liClientId);
        
        if (liClientPayload.getPersonalDetail() != null) {
            PersonalDetailDto personalDetail = liClientPayload.getPersonalDetail();
            policyDetails.setLaDob(personalDetail.getDob());
            policyDetails.setSalutation(personalDetail.getTitle());
            policyDetails.setInsuredFirstName(personalDetail.getFirstName());
            policyDetails.setInsuredLastName(personalDetail.getLastName());
            policyDetails.setGender(personalDetail.getGenderDesc());
            policyDetails.setFatherSpouse(personalDetail.getFatherOrSpouseName());
            policyDetails.setAtrFlag(personalDetail.getAtrFlag());
        }
        
        if (liClientPayload.getClientPolicyRelationship() != null && !liClientPayload.getClientPolicyRelationship().isEmpty()) {
            policyDetails.setCustomerRole(liClientPayload.getClientPolicyRelationship().get(0).getRelationship());
        }
        
        if (mainPayload.getPolicyBasicDetails() != null) {
            policyDetails.setProductName(mainPayload.getPolicyBasicDetails().getPolicyBasePlanIdDesc());
            policyDetails.setProductCode(mainPayload.getPolicyBasicDetails().getPolicyBasePlanIdCd());
        }
        
        if (liClientPayload.getContactDetail() != null && !liClientPayload.getContactDetail().isEmpty()) {
            policyDetails.setMobileNo(liClientPayload.getContactDetail().get(0).getMobNum());
        }
        
        if (liClientPayload.getIdentifiableInfo() != null) {
            String panNumber = liClientPayload.getIdentifiableInfo().getPanNum();
            policyDetails.setPanNo(panNumber);
            policyDetails.setLifeAssuredPANNumber(panNumber);
        }
        
        // Set EffectiveDateofCoverage from first coverage detail
        if (mainPayload.getCoverageDetails() != null && !mainPayload.getCoverageDetails().isEmpty()) {
            policyDetails.setEffectiveDateofCoverage(mainPayload.getCoverageDetails().get(0).getCvgIssEffDt());
        }
        
        // Set hardcoded values
        policyDetails.setJointLifeFlag("N");
        policyDetails.setSubLob("Individual");
        
        response.setPolicyDetails(policyDetails);
        
        return response;
    }
   
}

