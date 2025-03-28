package com.project.stationery_be_server.dto.request;

import java.util.Date;

public class UserUpdateRequest {
    String first_name;
    String last_name;
    String email;
    String phone;
    String password;
    Date dob;
    String avatar;
    boolean isBlock;
    Integer otp;
    RoleRequest roleRequest;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public RoleRequest getRoleRequest() {
        return roleRequest;
    }

    public void setRoleRequest(RoleRequest roleRequest) {
        this.roleRequest = roleRequest;
    }
}
