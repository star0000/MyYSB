package com.mds.myysb.bean;

public class GetAccountBean {

    /**
     * Deposit : 0
     * Expenditure : 0
     *
     * Reward : 0
     * Balance : 0
     */

    private int Deposit;
    private int Expenditure;
    private int Reward;
    private int Balance;

    public int getDeposit() {
        return Deposit;
    }

    public void setDeposit(int Deposit) {
        this.Deposit = Deposit;
    }

    public int getExpenditure() {
        return Expenditure;
    }

    public void setExpenditure(int Expenditure) {
        this.Expenditure = Expenditure;
    }

    public int getReward() {
        return Reward;
    }

    public void setReward(int Reward) {
        this.Reward = Reward;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int Balance) {
        this.Balance = Balance;
    }

}
