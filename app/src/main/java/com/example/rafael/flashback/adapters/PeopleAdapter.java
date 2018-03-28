package com.example.rafael.flashback.adapters;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukmai on 3/14/2018.
 */

public class PeopleAdapter implements Parcelable{
    private List<String> names;
    private List<String> emails;
    private String userId;
    private String userName;
    private String userEmail;

    public PeopleAdapter(List<String> names, List<String> emails, String id, String name, String email) {
        this.names = names;
        this.emails = emails;
        this.userId = id;
        this.userName = name;
        this.userEmail = email;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PeopleAdapter createFromParcel(Parcel in) {
            return new PeopleAdapter(in);
        }

        public PeopleAdapter[] newArray(int size) {
            return new PeopleAdapter[size];
        }
    };

    public PeopleAdapter(Parcel in) {
        this.names = new ArrayList<>();
        this.emails = new ArrayList<>();
        in.readStringList(names);
        in.readStringList(emails);
        this.userId = in.readString();
        this.userName = in.readString();
        this.userEmail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(names);
        parcel.writeStringList(emails);
        parcel.writeString(userId);
        parcel.writeString(userName);
        parcel.writeString(userEmail);
    }

    @Override
    public String toString() {
        return "UserId: " + userId + "\n" +
                "UserName: " + userName + "\n" +
                "UserEmail: " + userEmail + "\n" +
                "FriendsSize: " + Integer.toString(names.size()) + "\n";
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}