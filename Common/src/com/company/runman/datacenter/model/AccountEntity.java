package com.company.runman.datacenter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Edison on 2014/6/6.
 */
public class AccountEntity extends BaseResultEntity implements Parcelable {

    private String loginname;
    private String password;
    private String sitecode;
    private String fullname;
    private String email;
    private String phoneNum;
    private String phonetype;
    private String authoritylist;

    public AccountEntity() {
        loginname = "";
        password = "";
        sitecode = "defaultsite";
        fullname = "";
        email = "";
        phoneNum = "";
        phonetype = "android";
    }

    public String getAuthoritylist() {
        return authoritylist;
    }

    public void setAuthoritylist(String authoritylist) {
        this.authoritylist = authoritylist;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhonetype() {
        return phonetype;
    }

    public void setPhonetype(String phonetype) {
        this.phonetype = phonetype;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSitecode() {
        return sitecode;
    }

    public void setSitecode(String sitecode) {
        this.sitecode = sitecode;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEntity)) return false;

        AccountEntity that = (AccountEntity) o;

        if (sitecode != null ? !sitecode.equals(that.sitecode) : that.sitecode != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (loginname != null ? !loginname.equals(that.loginname) : that.loginname != null) return false;
        if (fullname != null ? !fullname.equals(that.fullname) : that.fullname != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (phoneNum != null ? !phoneNum.equals(that.phoneNum) : that.phoneNum != null) return false;
        if (phonetype != null ? !phonetype.equals(that.phonetype) : that.phonetype != null) return false;

        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(loginname);
        parcel.writeString(password);
        parcel.writeString(sitecode);
        parcel.writeString(fullname);
        parcel.writeString(email);
        parcel.writeString(phoneNum);
        parcel.writeString(phonetype);
    }

    public static final Parcelable.Creator<AccountEntity> CREATOR = new Parcelable.Creator<AccountEntity>() {
        //重写Creator
        @Override
        public AccountEntity createFromParcel(Parcel source) {
            AccountEntity account = new AccountEntity();
            account.loginname = source.readString();
            account.password = source.readString();
            account.sitecode = source.readString();
            account.fullname = source.readString();
            account.email = source.readString();
            account.phoneNum = source.readString();
            account.phonetype = source.readString();
            return account;
        }

        @Override
        public AccountEntity[] newArray(int size) {
            return null;
        }
    };
}
