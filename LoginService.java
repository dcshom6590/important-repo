package com.demo.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.demo.dto.ClientDto;
import com.demo.demo.dto.PolicyDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String API_TOKEN = "eyJraWQiOiJnRm5IUTVDdm9taUFmU0lmRjJwNGF6TkFQYTVpS0dUeGJmdW15Ym9UZFUwPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI0dWhuODU5bzd0MnJlMG1pMGxxOHE1anZyZiIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoiYWRtaW5cL3dyaXRlIGFkbWluXC9yZWFkIiwiYXV0aF90aW1lIjoxNzc2NDI4MDE3LCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuYXAtc291dGgtMS5hbWF6b25hd3MuY29tXC9hcC1zb3V0aC0xX2tMVkxMT2hGRCIsImV4cCI6MTc3NjQzMTYxNywiaWF0IjoxNzc2NDI4MDE3LCJ2ZXJzaW9uIjoyLCJqdGkiOiJiMTI1NjE3Ny0zOTI2LTQ2NmQtOWY5Ni0xYTQwNjUwOGRmNzMiLCJjbGllbnRfaWQiOiI0dWhuODU5bzd0MnJlMG1pMGxxOHE1anZyZiJ9.VdCdXGlojs7PhwMPG5Y_GVdxVUEggk_74DQHV-9mxzEJhsJgCA4CcfS8imFJmvPknmLkrQDyZmoAuBixxY8GAdXuV9lXtHz1p3EZh_yqYXiBU9SzMafb8dFOPGMwwC8QJ3aMpSQyTyxHQWrN08Tpqw9bBnV_uHi2IMsJZ8WtOWaxXIPt0TDzjUO_q806iExLb9FaRMsbLA2hmPUqgS9QP1R61CqyUS0c8n8NTk6Ghdsmo3ajkfJAjKFQn1AVpA2CWjGvBuHQgS0IM_yibUSFW62qPJOUk5cnIfsQoHIiA0hCRGr1EnG4-NintQMTbyFQSEBMlZMB3SXD8zT-F5DAhA";
    
    public PolicyDto getPolicyDetails(String polId) {
        // Mock data map
        Map<String, String> policies = new HashMap<>();
        policies.put("875697583", """
{
  "response": {
    "header": {
      "soaCorrelationId": "2841",
      "soaAppId": "CLAIMSWEBSITEJOURNEY"
    },
    "msgInfo": {
      "msgCode": "200",
      "msg": "Success",
      "msgDescription": "policyClientRelationship : Data Not Found !!policyClientRelationship : Data Not Found !!"
    },
    "payload": {
      "policyNo": "875697583",
      "planName": "Axis Max Life Life Gain Plus 20 Yr 10 Pay",
      "planType": "Traditional",
      "policyStatusCode": "1",
      "policyStatusDesc": "Premium paying (regular)",
      "policyHolderName": "MR. CHINTALA SHANKER",
      "mobileNo": "8882280228",
      "dob": "1990-07-30",
      "emailID": "9999",
      "policyBasicDetails": {
        "policyNum": "875697583",
        "policyBasePlanIdCd": "EP2010",
        "policyBasePlanIdDesc": "Axis Max Life Life Gain Plus 20 Yr 10 Pay",
        "policyOwnerId": "0003394514",
        "policyInsuredId": "0003394514",
        "policyPayerId": "",
        "policyAssigneeId": "",
        "reasonForAssgnmt": "",
        "effDtOfAssgnmt": "",
        "policyTrusteeId": "",
        "policyStatusCd": "1",
        "policyStatusDesc": "Premium paying (regular)",
        "policyPrevStatusCd": "C",
        "policyPrevStatusDesc": "Complete",
        "policyIssueDt": "2020-03-16",
        "policyInforceDt": "2026-03-16",
        "pranNumber": "",
        "billingFreqCd": "12",
        "billingFreqDesc": "Annual",
        "modeFactor": "1",
        "policyBillingTypeCd": "1",
        "policyBillingTypeDesc": "Direct Bill",
        "policyxsellFlag": "",
        "policyhhFlag": "",
        "lastPremPmtMethodCd": "",
        "lastPremPmtMethodDesc": "",
        "initialPremMethodCd": "",
        "initialPremMethodDesc": "",
        "lastPremPmtDt": "",
        "polAppRecvDt": "2020-03-16",
        "polAppSignDt": "2020-03-16",
        "initialPremDt": "",
        "policyPmtTerm": "10",
        "policyTerm": "20",
        "policyServicingAgentId": "100810",
        "policyServicingGoCode": "ASNG1",
        "policyWritingGoCode": "ASNG1",
        "policyStatusChangeDt": "2026-03-16",
        "polCeasDt": "2040-03-16",
        "policyRejectionReasonCd": "",
        "policyRejectionReasonDesc": "",
        "policyInsuranceTypeCd": "2",
        "policyInsuranceTypeDesc": "Pure Endowment",
        "policyPtToDtNum": "2027-03-16",
        "policyCntlPrPtToDt": "",
        "policyPmtDrwDy": "0",
        "policyDivOptCd": "1",
        "policyDivOptDesc": "Paid in cash",
        "policyDbOptCd": "N",
        "policyDbOptDesc": "Not applicable; default value",
        "policyPremTypeCd": "E",
        "policyPremTypeDesc": "Traditional, contractual premium only (including traditional deferred annuity)",
        "policySector": "",
        "ceip": "N",
        "policyCampnCd": "",
        "policyCampnDesc": "",
        "policyKeyManInd": "N",
        "policyTds10": "Y",
        "policyEiaInd": "N",
        "policyCeasRsnCd": "R",
        "policyCeasRsnDesc": "Automatic Cease Reason",
        "policyDiscontineuRsnCd": "",
        "policyDiscontineuRsnDesc": "",
        "policyLapseDt": "",
        "ulLapseRevivalIndicator": "N",
        "polCumUnclrdAmt": "0.00",
        "freeLookEndDt": "2020-03-16",
        "policySubStatusCd": "",
        "policySubStatusDesc": "",
        "policyDicontinuedDt": "",
        "era": "",
        "newProductFlagging": "",
        "stpDfaIsActiveOrNot": "",
        "indxLevelFlagIndicator": "NO",
        "irdaGuidelineTypeCd": "N/A",
        "irdaGuidelineTypeDesc": "Not Applicable",
        "ntuReasonCd": "",
        "ntuReasonDesc": "",
        "policyHasDeathBenefitGuarantee": "0.00",
        "comboPolicyId": "",
        "caseStatusCd": "",
        "caseStatusDesc": "",
        "serviceProviderName": "",
        "discontinuanceFund": "",
        "ntuEffDate": "",
        "numOfModalPremiumDue": "0",
        "totalServiceTaxOrGST": "0.00",
        "ctpAmt": "0.00",
        "polDueDate": "",
        "polLapseDate": "",
        "revivalDueDate": "",
        "isEligibleForLoan": "Y",
        "mwpaFlag": "P",
        "polprimInsrd": "0003394514",
        "polInsPurpCd": "P",
        "polInsPurp": "Personal Reasons",
        "form2Policy": "NO",
        "polRejectionReason": "",
        "employerEmployeePol": "NO",
        "keyManPolicy": "N",
        "tdsExempt": "Y",
        "policyAssignedMaxLife": "NO",
        "ownerEarnedIncome": "120000",
        "servicingAgentId": "100810",
        "servicingAgentBranchId": "ASNG1",
        "polAnnvDt": "2026-03-16 00:00:00.0",
        "polStatusReasnCd": "",
        "polStatusReasn": "",
        "planInsTypCd": "2",
        "polSurrRsnCd": "",
        "polSurrRsnDesc": "",
        "ptdNonHybrid": "",
        "prevAnniversaryDt": "2026-03-16 00:00:00.0",
        "parNonPar": "PARTICIPATING",
        "p2SaRatio": "7.81",
        "grcPeriodEndDtWithExtn": "2027-04-15 00:00:00.0",
        "grcPeriodEndDt": "2027-04-15 00:00:00.0",
        "mwpaSection": "",
        "policySuspensionRsnCd": "",
        "policySuspensionRsn": "",
        "productType": "Traditional",
        "policyWritingAgentId": "100810",
        "planTypeDesc": "Traditional",
        "assignPolicy": "NOT ASSIGN",
        "maxLifeRegisteredState": "MH",
        "maxLifeRegisteredStateNm": "Maharashtra",
        "ingBatchDate": "18-09-2026",
        "channelCustomerid": "",
        "channelCustomerClass": "",
        "previousBillingTypeInfo": "",
        "currentPremiumDueAmt": "0.00",
        "totalPremiumDueAmt": "0.00",
        "drawDate": "",
        "initialPolicyTerm": "20",
        "premSusp": "720392.28",
        "advSusp": "0.00",
        "divSuspAmt": "0.00",
        "premDepSuspAmt": "0.00",
        "outDispAmt": "0.00",
        "miscSuspAmt": "0.00",
        "defermentPeriod": "0",
        "incomePeriod": "0",
        "incomePaymentFrequency": "",
        "annualizedPremium": "39065.00",
        "policyOwnerFlag": "N",
        "polCashSurrAmt": "0.00",
        "polReisnType": "",
        "tolPremLastReisn": "",
        "polReisnDt": "",
        "termPlanInd": "No",
        "cancerPlanInd": "No",
        "planCsvInd": "Applicable",
        "productName": "Axis Max Life Life Gain Plus 20 Yr 10 Pay",
        "goGreenInd": "",
        "compnyID": "CP",
        "policyGcInd": "N"
      },
      "coverageDetails": [
        {
          "cvgNum": "01",
          "cvgPlanIdCd": "EP2010",
          "coverageStatusCd": "1",
          "cvgFaceAmt": "500000.00",
          "cvgDutyAmt": "100.00",
          "coverageModelPrem": "39065.00",
          "cvgBasicFaceAmt": "500000.00",
          "cvgUwgFrcnCd": "AA",
          "cvgActTypCd": "B",
          "cvgAppRevDt": "2020-03-16",
          "cvgMatXpryDt": "2040-03-16",
          "cvgIssEffDt": "2020-03-16",
          "origPlanIdCd": "EP2010",
          "cvgRateAge": "029",
          "cvgFePremAmt": "0.00",
          "cvgParCd": "P",
          "cvgStatEffDt": "2020-03-16",
          "premChngAgeDur": "010",
          "premChngeDtCd": "A",
          "ulrcvgYn": "N",
          "ulpcvgYn": "N",
          "riderServTax": "0.00",
          "noOfCoverage": "1",
          "cvgStatusChangeDt": "2020-03-16",
          "cvgPlanIdDesc": "Axis Max Life Life Gain Plus 20 Yr 10 Pay",
          "coverageStatusDesc": "PREMIUM PAYING",
          "cvgUwgFrcnDesc": "Approved by application entry phase",
          "cvgActTypDesc": "Benefits",
          "origPlanIdDesc": "Axis Max Life Life Gain Plus 20 Yr 10 Pay",
          "riderTerm": "20",
          "stgstTax": "0.00",
          "riderFlg": "",
          "zeroCovergeFaceAmt": "500000.00",
          "riderAnnualPremium": "39065.00",
          "cvgUwdcnCd": "AA",
          "loading": "NO",
          "cvgSumInsAmt": "500000.00",
          "deathBenefit": "",
          "covLapseReasonCd": " ",
          "covLapseReasonDesc": "",
          "coverageMultiple": " ",
          "feeTypeCoverMultiple": " ",
          "productTypeRider": "2",
          "riskClass": "S",
          "customerDiscount": " ",
          "premiumBreakOption": " ",
          "deathBenefitOption": "",
          "riderPremium": "",
          "riderInbuiltIdentifier": ""
        }
      ],
      "policyClientRelationship": [
        {
          "clientID": "0009512957",
          "roleEfftDt": "",
          "relatnReason": "",
          "relatnType": "",
          "prcntShareOfNom": "",
          "prefAddrCode": "",
          "relatnCat": "",
          "nomineeCliId": "",
          "polNomineeAccSlct": "",
          "polOwnerAccSlct": "",
          "polAssgnTypeCd": "",
          "polAssgnType": "",
          "nomineeGender": "",
          "tusteeCliId": ""
        },
        {
          "clientID": "0003394514",
          "roleEfftDt": "",
          "relatnReason": "",
          "relatnType": "I",
          "prcntShareOfNom": "",
          "prefAddrCode": "",
          "relatnCat": "",
          "nomineeCliId": "0003394514",
          "polNomineeAccSlct": "",
          "polOwnerAccSlct": "",
          "polAssgnTypeCd": "",
          "polAssgnType": "",
          "nomineeGender": "",
          "tusteeCliId": ""
        },
        {
          "clientID": "",
          "roleEfftDt": "",
          "relatnReason": "",
          "relatnType": "",
          "prcntShareOfNom": "",
          "prefAddrCode": "",
          "relatnCat": "",
          "nomineeCliId": "",
          "polNomineeAccSlct": "",
          "polOwnerAccSlct": "",
          "polAssgnTypeCd": "",
          "polAssgnType": "",
          "nomineeGender": "",
          "tusteeCliId": ""
        }
      ],
      "nomineeDetails": {
        "policyOwnerId": "0003394514",
        "policyHolderName": "MR. CHINTALA SHANKER",
        "mobileNo": "8882280228",
        "dob": "1990-07-30",
        "emailID": "9999",
        "appointeeClientID": "",
        "appointeeName": "",
        "appointeeDob": "",
        "nominees": [
          
        ]
      }
    }
  }
}
""");
policies.put("371714601", """
{
  "response": {
    "header": {
      "soaCorrelationId": "2841",
      "soaAppId": "CLAIMSWEBSITEJOURNEY"
    },
    "msgInfo": {
      "msgCode": "200",
      "msg": "Success",
      "msgDescription": "policyClientRelationship : Data Not Found !!policyClientRelationship : Data Not Found !!"
    },
    "payload": {
      "policyNo": "371714601",
      "planName": "Axis Max Life Whole Life Participating Plan",
      "planType": "Traditional",
      "policyStatusCode": "1",
      "policyStatusDesc": "Premium paying (regular)",
      "policyHolderName": "MR. CHINTALA SHANKER",
      "mobileNo": "8882280228",
      "dob": "1990-07-30",
      "emailID": "9999",
      "policyBasicDetails": {
        "policyNum": "371714601",
        "policyBasePlanIdCd": "WP00",
        "policyBasePlanIdDesc": "Axis Max Life Whole Life Participating Plan",
        "policyOwnerId": "0003394514",
        "policyInsuredId": "0003394514",
        "policyPayerId": "0003394514",
        "policyAssigneeId": "",
        "reasonForAssgnmt": "",
        "effDtOfAssgnmt": "",
        "policyTrusteeId": "",
        "policyStatusCd": "1",
        "policyStatusDesc": "Premium paying (regular)",
        "policyPrevStatusCd": "3",
        "policyPrevStatusDesc": "Paid-up life",
        "policyIssueDt": "2010-02-19",
        "policyInforceDt": "2010-02-23",
        "pranNumber": "",
        "billingFreqCd": "12",
        "billingFreqDesc": "Annual",
        "modeFactor": "1",
        "policyBillingTypeCd": "U",
        "policyBillingTypeDesc": "UPI",
        "policyxsellFlag": "",
        "policyhhFlag": "",
        "lastPremPmtMethodCd": "",
        "lastPremPmtMethodDesc": "",
        "initialPremMethodCd": "",
        "initialPremMethodDesc": "",
        "lastPremPmtDt": "2025-02-19",
        "polAppRecvDt": "2010-02-21",
        "polAppSignDt": "2010-02-19",
        "initialPremDt": "2010-02-23",
        "policyPmtTerm": "81",
        "policyTerm": "81",
        "policyServicingAgentId": "823139",
        "policyServicingGoCode": "EHYD1",
        "policyWritingGoCode": "AHYD1",
        "policyStatusChangeDt": "2021-02-25",
        "polCeasDt": "2091-02-19",
        "policyRejectionReasonCd": "",
        "policyRejectionReasonDesc": "",
        "policyInsuranceTypeCd": "7",
        "policyInsuranceTypeDesc": "Whole Life",
        "policyPtToDtNum": "2025-02-19",
        "policyCntlPrPtToDt": "",
        "policyPmtDrwDy": "20",
        "policyDivOptCd": "3",
        "policyDivOptDesc": "Buy paid-up additions",
        "policyDbOptCd": "N",
        "policyDbOptDesc": "Not applicable; default value",
        "policyPremTypeCd": "E",
        "policyPremTypeDesc": "Traditional, contractual premium only (including traditional deferred annuity)",
        "policySector": "",
        "ceip": "N",
        "policyCampnCd": "",
        "policyCampnDesc": "",
        "policyKeyManInd": "N",
        "policyTds10": "Y",
        "policyEiaInd": "N",
        "policyCeasRsnCd": "R",
        "policyCeasRsnDesc": "Automatic Cease Reason",
        "policyDiscontineuRsnCd": "",
        "policyDiscontineuRsnDesc": "",
        "policyLapseDt": "",
        "ulLapseRevivalIndicator": "N",
        "polCumUnclrdAmt": "0.00",
        "freeLookEndDt": "2010-03-20",
        "policySubStatusCd": "",
        "policySubStatusDesc": "",
        "policyDicontinuedDt": "",
        "era": "",
        "newProductFlagging": "",
        "stpDfaIsActiveOrNot": "",
        "indxLevelFlagIndicator": "NO",
        "irdaGuidelineTypeCd": "N/A",
        "irdaGuidelineTypeDesc": "Not Applicable",
        "ntuReasonCd": "",
        "ntuReasonDesc": "",
        "policyHasDeathBenefitGuarantee": "0.00",
        "comboPolicyId": "",
        "caseStatusCd": "",
        "caseStatusDesc": "",
        "serviceProviderName": "",
        "discontinuanceFund": "",
        "ntuEffDate": "",
        "numOfModalPremiumDue": "",
        "totalServiceTaxOrGST": "",
        "ctpAmt": "",
        "polDueDate": "",
        "polLapseDate": "",
        "revivalDueDate": "",
        "isEligibleForLoan": "Y",
        "mwpaFlag": "P",
        "polprimInsrd": "0003394514",
        "polInsPurpCd": "P",
        "polInsPurp": "Personal Reasons",
        "form2Policy": "NO",
        "polRejectionReason": "",
        "employerEmployeePol": "NO",
        "keyManPolicy": "N",
        "tdsExempt": "Y",
        "policyAssignedMaxLife": "NO",
        "ownerEarnedIncome": "120000",
        "servicingAgentId": "823139",
        "servicingAgentBranchId": "EHYD1",
        "polAnnvDt": "2024-02-19 00:00:00.0",
        "polStatusReasnCd": "",
        "polStatusReasn": "",
        "planInsTypCd": "7",
        "polSurrRsnCd": "",
        "polSurrRsnDesc": "",
        "ptdNonHybrid": "",
        "prevAnniversaryDt": "2024-02-19 00:00:00.0",
        "parNonPar": "PARTICIPATING",
        "p2SaRatio": "",
        "grcPeriodEndDtWithExtn": "2025-03-21 00:00:00.0",
        "grcPeriodEndDt": "2025-03-21 00:00:00.0",
        "mwpaSection": "",
        "policySuspensionRsnCd": "",
        "policySuspensionRsn": "",
        "productType": "Traditional",
        "policyWritingAgentId": "218608",
        "planTypeDesc": "Traditional",
        "assignPolicy": "NOT ASSIGN",
        "maxLifeRegisteredState": "TG",
        "maxLifeRegisteredStateNm": "Telangana",
        "ingBatchDate": "18-09-2026",
        "channelCustomerid": "",
        "channelCustomerClass": "",
        "previousBillingTypeInfo": "C,1",
        "currentPremiumDueAmt": "",
        "totalPremiumDueAmt": "",
        "drawDate": "",
        "initialPolicyTerm": "81",
        "premSusp": "0.00",
        "advSusp": "0.00",
        "divSuspAmt": "0.00",
        "premDepSuspAmt": "0.00",
        "outDispAmt": "0.00",
        "miscSuspAmt": "0.00",
        "defermentPeriod": "0",
        "incomePeriod": "0",
        "incomePaymentFrequency": "",
        "annualizedPremium": "4999.72",
        "policyOwnerFlag": "N",
        "polCashSurrAmt": "0.00",
        "polReisnType": "",
        "tolPremLastReisn": "",
        "polReisnDt": "",
        "termPlanInd": "No",
        "cancerPlanInd": "No",
        "planCsvInd": "Applicable",
        "productName": "Axis Max Life Whole Life Participating Plan",
        "goGreenInd": "",
        "compnyID": "CP",
        "policyGcInd": ""
      },
      "coverageDetails": [
        {
          "cvgNum": "01",
          "cvgPlanIdCd": "WP00",
          "coverageStatusCd": "1",
          "cvgFaceAmt": "291529.00",
          "cvgDutyAmt": "58.30",
          "coverageModelPrem": "4999.72",
          "cvgBasicFaceAmt": "291529.00",
          "cvgUwgFrcnCd": "AC",
          "cvgActTypCd": "B",
          "cvgAppRevDt": "2010-02-21",
          "cvgMatXpryDt": "2091-02-19",
          "cvgIssEffDt": "2010-02-19",
          "origPlanIdCd": "WP00",
          "cvgRateAge": "019",
          "cvgFePremAmt": "0.00",
          "cvgParCd": "P",
          "cvgStatEffDt": "2018-02-19",
          "premChngAgeDur": "000",
          "premChngeDtCd": "0",
          "ulrcvgYn": "N",
          "ulpcvgYn": "N",
          "riderServTax": "112.50",
          "noOfCoverage": "1",
          "cvgStatusChangeDt": "2018-02-19",
          "cvgPlanIdDesc": "Axis Max Life Whole Life Participating Plan",
          "coverageStatusDesc": "PREMIUM PAYING",
          "cvgUwgFrcnDesc": "Approved by the clear case process",
          "cvgActTypDesc": "Benefits",
          "origPlanIdDesc": "Axis Max Life Whole Life Participating Plan",
          "riderTerm": "81",
          "stgstTax": "112.50",
          "riderFlg": "",
          "zeroCovergeFaceAmt": "291529.00",
          "riderAnnualPremium": "4999.72",
          "cvgUwdcnCd": "AC",
          "loading": "NO",
          "cvgSumInsAmt": "291529.00",
          "deathBenefit": "",
          "covLapseReasonCd": " ",
          "covLapseReasonDesc": "",
          "coverageMultiple": " ",
          "feeTypeCoverMultiple": " ",
          "productTypeRider": "7",
          "riskClass": "S",
          "customerDiscount": " ",
          "premiumBreakOption": " ",
          "deathBenefitOption": "",
          "riderPremium": "",
          "riderInbuiltIdentifier": ""
        }
      ],
      "policyClientRelationship": [
        {
          "clientID": "5002275613",
          "roleEfftDt": "",
          "relatnReason": "",
          "relatnType": "",
          "prcntShareOfNom": "",
          "prefAddrCode": "",
          "relatnCat": "",
          "nomineeCliId": "",
          "polNomineeAccSlct": "",
          "polOwnerAccSlct": "",
          "polAssgnTypeCd": "",
          "polAssgnType": "",
          "nomineeGender": "",
          "tusteeCliId": ""
        },
        {
          "clientID": "0003394514",
          "roleEfftDt": "",
          "relatnReason": "",
          "relatnType": "I",
          "prcntShareOfNom": "",
          "prefAddrCode": "",
          "relatnCat": "",
          "nomineeCliId": "0003394514",
          "polNomineeAccSlct": "",
          "polOwnerAccSlct": "",
          "polAssgnTypeCd": "",
          "polAssgnType": "",
          "nomineeGender": "",
          "tusteeCliId": ""
        },
        {
          "clientID": "",
          "roleEfftDt": "",
          "relatnReason": "",
          "relatnType": "",
          "prcntShareOfNom": "",
          "prefAddrCode": "",
          "relatnCat": "",
          "nomineeCliId": "",
          "polNomineeAccSlct": "",
          "polOwnerAccSlct": "",
          "polAssgnTypeCd": "",
          "polAssgnType": "",
          "nomineeGender": "",
          "tusteeCliId": ""
        }
      ],
      "nomineeDetails": {
        "policyOwnerId": "0003394514",
        "policyHolderName": "MR. CHINTALA SHANKER",
        "mobileNo": "8882280228",
        "dob": "1990-07-30",
        "emailID": "9999",
        "appointeeClientID": "",
        "appointeeName": "",
        "appointeeDob": "",
        "nominees": [
          {
            "nomineeClientID": "0009512957",
            "nomineeName": "MISS PRABHA RANI",
            "percentageShare": "100",
            "nomineeRelation": "Brother",
            "nomineeDob": "1998-04-08"
          }
        ]
      }
    }
  }
}
""");
        
        // Get mock data if available, otherwise call real API
        if (policies.containsKey(polId)) {
            try {
                return objectMapper.readValue(policies.get(polId), PolicyDto.class);
            } catch (Exception e) {
                throw new RuntimeException("Error parsing mock policy data", e);
            }
        }
        
        // Real API call (commented out)
        // try {
        //     String url = "https://gatewayclouduat.maxlifeinsurance.com/uat/policy360/getpolicydetails";
            
        //     // Set headers
        //     HttpHeaders headers = new HttpHeaders();
        //     headers.set("x-api-key", "X40OMfAKAh3Fz62CNl6FUDheG3mCgum2heDrLHk2");
        //     headers.set("x-apigw-api-id", "puaewiy985");
        //     headers.set("Authorization", "Bearer " + API_TOKEN);
        //     headers.set("APPID", "CLAIMSWEBSITEJOURNEY");
        //     headers.set("Content-Type", "application/json");
            
        //     // Build request body
        //     Map<String, Object> requestBody = new HashMap<>();
        //     Map<String, Object> request = new HashMap<>();
        //     Map<String, Object> header = new HashMap<>();
        //     header.put("soaCorrelationId", "2841");
        //     header.put("soaAppId", "CLAIMSWEBSITEJOURNEY");
            
        //     Map<String, Object> payload = new HashMap<>();
        //     payload.put("policyNo", polId);
            
        //     List<Map<String, String>> services = Arrays.asList(
        //         Map.of("serviceName", "policyClientRelationship", "clientRole", "T"),
        //         Map.of("serviceName", "policyClientRelationship", "clientRole", "I"),
        //         Map.of("serviceName", "nomineeDetails"),
        //         Map.of("serviceName", "policyBasicDetails"),
        //         Map.of("serviceName", "coverageDetails")
        //     );
        //     payload.put("services", services);
            
        //     request.put("header", header);
        //     request.put("payload", payload);
        //     requestBody.put("request", request);
            
        //     HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
        //     ResponseEntity<PolicyDto> response = restTemplate.exchange(
        //         url,
        //         HttpMethod.POST,
        //         entity,
        //         PolicyDto.class
        //     );
            
        //     return response.getBody();
        // } catch (Exception e) {
        //     throw new RuntimeException("Error calling policy API", e);
        // }
        
        throw new RuntimeException("Policy not found: " + polId);
    }
    
    public ClientDto getClientDetails(String clientId) {
        try {
            String url = "https://gatewayclouduat.maxlifeinsurance.com/uat/getcustomerdetails";
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", "X40OMfAKAh3Fz62CNl6FUDheG3mCgum2heDrLHk2");
            headers.set("x-apigw-api-id", "puaewiy985");
            headers.set("Authorization", "Bearer " + API_TOKEN);
            headers.set("APPID", "CLAIMSWEBSITEJOURNEY");
            headers.set("Content-Type", "application/json");
            
            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> request = new HashMap<>();
            Map<String, Object> header = new HashMap<>();
            header.put("soaCorrelationId", "11827g07t");
            header.put("soaAppId", "CLAIMSWEBSITEJOURNEY");
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("clientId", clientId);
            
            List<Map<String, String>> services = Arrays.asList(
                Map.of("serviceName", "baseAddDetail"),
                Map.of("serviceName", "personalDetail"),
                Map.of("serviceName", "contactDetail"),
                Map.of("serviceName", "identifiableInfo"),
                Map.of("serviceName", "clientPolicyRelationship")
            );
            payload.put("services", services);
            
            request.put("header", header);
            request.put("payload", payload);
            requestBody.put("request", request);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<ClientDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                ClientDto.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error calling client API", e);
        }
    }
    
    public void validatePolicy(PolicyDto policy) {
        PolicyDto.Payload payload = policy.getResponse().getPayload();
        
        // VALIDATION 1: Check for Joint Life Policy using coverageDetails
        List<String> jointLifePlanCodes = Arrays.asList(
            "IAJ", "IAJC", "IAJR", "IAJRC", "TDAJ", "TDAJC", "IALJ", "IALJC", "IALJRP", "IALRJC",
            "TIAJRC", "TIAJR", "TIAGJ", "TIAGJF", "TIAGJR", "TDAJN", "TDAJNC", "TSPJS", "TSPJR",
            "TSPJL", "TSPJ6", "TSRJS", "TSRJR", "TSRJL", "TSRJ6", "TIRJ", "TDRJ", "TIRJR", "TDRJR",
            "TDRJN", "TDRJNR", "TIGRJ", "TIGRJR", "TDGRJR", "TPFDG2", "TPFDG1", "TSFDG2", "TSFDG1",
            "TSWRW2", "TSWRW1", "TSWPW1", "TSWPW2", "TIRJFR", "TIRJF", "TIRJRF", "TIRJRP", "TDGRJ"
        );
        
        if (payload.getCoverageDetails() != null) {
            boolean isJointLife = payload.getCoverageDetails().stream()
                .anyMatch(coverage -> {
                    String planCode = coverage.getCvgPlanIdCd();
                    return planCode != null && jointLifePlanCodes.contains(planCode.trim());
                });
            
            if (isJointLife) {
                throw new RuntimeException("Joint Life Policy not allowed");
            }
        }
        
        // VALIDATION 2: Check for Life Insured via policyClientRelationship
        List<com.demo.demo.dto.PolicyClientRelationshipDto> policyClientRelationship = 
            payload.getPolicyClientRelationship();
        
        if (policyClientRelationship != null) {
            long insuredCount = policyClientRelationship.stream()
                .filter(relation -> {
                    String relatnType = relation.getRelatnType();
                    return relatnType != null && "I".equals(relatnType.trim());
                })
                .count();
            
            if (insuredCount == 0) {
                throw new RuntimeException("At least 1 Life Insured is required");
            }
        } else {
            throw new RuntimeException("At least 1 Life Insured is required");
        }
        
        // VALIDATION 3: Check for Assignees using policyAssigneeId from policyBasicDetails
        if (payload.getPolicyBasicDetails() != null) {
            String policyAssigneeId = payload.getPolicyBasicDetails().getPolicyAssigneeId();
            if (policyAssigneeId != null && !policyAssigneeId.trim().isBlank()) {
                throw new RuntimeException("Assignee not allowed");
            }
        }
    }
}