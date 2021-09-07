package br.com.leonardozv.ledger.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JournalEntryCreatedEventData {

    @JsonProperty("accounting_date")
    private String accountingDate;

    @JsonProperty("company_id")
    private String companyId;

    @JsonProperty("gaap_id")
    private Integer gaapId;

    @JsonProperty("balance_version_id")
    private Integer balanceVersionId;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("credit_amount")
    private Double creditAmount;

    @JsonProperty("debit_amount")
    private Double debitAmount;

    @JsonProperty("movement_amount")
    private Double movementAmount;

    public JournalEntryCreatedEventData() {

    }

    public JournalEntryCreatedEventData(String accountingDate, String companyId, Integer gaapId, Integer balanceVersionId, String accountId, Double creditAmount, Double debitAmount, Double movementAmount) {
        this.accountingDate = accountingDate;
        this.companyId = companyId;
        this.gaapId = gaapId;
        this.balanceVersionId = balanceVersionId;
        this.accountId = accountId;
        this.creditAmount = creditAmount;
        this.debitAmount = debitAmount;
        this.movementAmount = movementAmount;
    }

    public String getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(String accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getGaapId() {
        return gaapId;
    }

    public void setGaapId(Integer gaapId) {
        this.gaapId = gaapId;
    }

    public Integer getBalanceVersionId() {
        return balanceVersionId;
    }

    public void setBalanceVersionId(Integer balanceVersionId) {
        this.balanceVersionId = balanceVersionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getMovementAmount() {
        return movementAmount;
    }

    public void setMovementAmount(Double movementAmount) {
        this.movementAmount = movementAmount;
    }

}
