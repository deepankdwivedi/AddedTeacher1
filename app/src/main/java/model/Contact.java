package model;

public class Contact {

public int _id;
public int tea_id ;
public String username ;
public String password;
public String sch_id;
public String tea_name;
public String dob;
public String address;
public String photo;
public long mobile;
public String email;
public String cls_teacher;
public int last_update;
public String status;


public int getID() {
return this._id;
}

public void setID(int id) {
// TODO Auto-generated method stub
this._id = id;
}


public int getTea_id() {
	return tea_id;
}

public void setTea_id(int tea_id) {
	this.tea_id = tea_id;
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getSch_id() {
	return sch_id;
}

public void setSch_id(String sch_id) {
	this.sch_id = sch_id;
}

public String getTea_name() {
	return tea_name;
}

public void setTea_name(String tea_name) {
	this.tea_name = tea_name;
}

public String getDob() {
	return dob;
}

public void setDob(String dob) {
	this.dob = dob;
}

public String getAddress() {
	return address;
}

public void setAddress(String address) {
	this.address = address;
}

public String getPhoto() {
	return photo;
}

public void setPhoto(String photo) {
	this.photo = photo;
}

public long getMobile() {
	return mobile;
}

public void setMobile(int mobile) {
	this.mobile = mobile;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getCls_teacher() {
	return cls_teacher;
}

public void setCls_teacher(String cls_teacher) {
	this.cls_teacher = cls_teacher;
}

public int getLast_update() {
	return last_update;
}

public void setLast_update(int last_update) {
	this.last_update = last_update;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

// constructor
public Contact (int id, String sch_id, String tea_name, String dob, String address, String photo, int mobile, String email, String cls_teacher, int last_update, String status){
this._id = id;
this.sch_id=sch_id;
this.tea_name=tea_name;
this.dob=dob;
this.address=address;
this.photo=photo;
this.mobile=mobile;
this.email=email;
this.cls_teacher=cls_teacher;
this.last_update=last_update;
this.status=status;
}

public Contact (){

}
}
