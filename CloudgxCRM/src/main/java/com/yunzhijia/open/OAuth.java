package com.yunzhijia.open;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author moon
 */
public class OAuth{

    private final static String YZJ_BASE_URL 
            = "https://www.yunzhijia.com";  
    private final static String APPAUTH2_URL =YZJ_BASE_URL
            +"/openauth2/api/appAuth2";    
    private final static String PERSON_GETALL_URL=YZJ_BASE_URL
            +"/openaccess/input/person/getall";
    private final static String ORG_GETALL_URL = YZJ_BASE_URL
            + "/openaccess/input/dept/getall";    
    private final static String CONTEXT_URL = YZJ_BASE_URL
            +"/openauth2/api/getcontext";
    /**
     * Creates a new instance of OAuth
     */
    public OAuth() {
    }

    /**
     * 获取所有组织数据
     * @param eid
     * @param keyFile
     * @return 
     */
    public static ArrayList<Department> getDepartment(String eid,File keyFile){
        ArrayList<Department> departments=new ArrayList();
        try {
            byte[] keyByte = EncryptUtils.getBytesFromFile(keyFile);
            Key key = EncryptUtils.restorePrivateKey(keyByte);            
            
            JsonNode jsonRequestData = new JsonNode("{}"); // json data without encrypt
            HttpResponse<JsonNode> jsonResponse = Unirest
                    .post(ORG_GETALL_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("eid", eid)
                    .field("nonce", UUID.randomUUID().toString())
                    .field("data",EncryptUtils.encryptWithEncodeBase64UTF8(jsonRequestData.toString(), key))
                    .asJson();
            JSONObject jsonResponseData = jsonResponse.getBody().getObject();
//            System.out.println(jsonResponseData);
            boolean success = jsonResponseData.getBoolean("success");
            if(success){
                JSONArray data = (JSONArray)jsonResponseData.get("data");
                for(Object o:data){
                    Department dep =new Department();
                    JSONObject d=(JSONObject) o;
                    
                    dep.setName(d.getString("name"));
                    dep.setUid(d.getString("id"));
                    dep.setFullName(d.getString("department"));
                    if(!d.isNull("parentId")){
                        dep.setParentid(d.getString("parentId"));
                    }else{
                        
                    }
                    departments.add(dep);
                }
            }
            return departments;
            
        } catch (IOException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnirestException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        return departments;
    }
    /**
     * 获取组织所有人员数据
     * @param eid
     * @param keyFile
     * @return 
     */
    public static ArrayList<Person> getPerson(String eid,File keyFile){
        ArrayList<Person> persons=new ArrayList();
        try {
            
            byte[] keyByte = EncryptUtils.getBytesFromFile(keyFile);
            Key key = EncryptUtils.restorePrivateKey(keyByte);
            
            JsonNode jsonRequestData = new JsonNode("{}");           
            HttpResponse<JsonNode> jsonResponse = Unirest
                    .post(PERSON_GETALL_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("eid", eid)
                    .field("nonce", UUID.randomUUID().toString())
                    .field("data",EncryptUtils.encryptWithEncodeBase64UTF8(jsonRequestData.toString(), key))
                    .asJson();
            JSONObject jsonResponseData = jsonResponse.getBody().getObject();
            boolean success = jsonResponseData.getBoolean("success");
            if(success){
                JSONArray data = (JSONArray)jsonResponseData.get("data");
                for(Object o:data){
                    Person person=new Person();
                    JSONObject p=(JSONObject) o;
                    person.setName(p.getString("name"));
                    person.setOpenid(p.getString("openId"));
                    person.setPhone(p.getString("phone"));
                    person.setEmail(p.getString("email"));                    
                    person.setStatus(p.getInt("status"));
                    
                    person.setDepartmentid(p.getString("departmentid"));
                    
                    persons.add(person);
                }
            }
            return persons;
            
        } catch (IOException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnirestException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        return persons;
    }

    /**
     * 获取云之家APP当前登录用户上下文
     * @param appid
     * @param appSecret
     * @return 
     */
    public static String getContext(String appid, String appSecret){
        try {
            String access_token=getAccess_token(appid, appSecret);
            String ticket="";
            HttpResponse<JsonNode> jsonResponse = Unirest
                    .get(CONTEXT_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .queryString("access_token", access_token)
                    .queryString("ticket", ticket)
                    .asJson();
            JsonNode jsonData = jsonResponse.getBody();
            return jsonData.toString();
        } catch (UnirestException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    /**
     * 获取轻应用的access_token
     * @param appid
     * @param appSecret
     * @return 
     */
    public static String getAccess_token(String appid, String appSecret){
        try {
            String authorization=AppAuth2.appAuth2Treaty(appid,appSecret);
            HttpResponse<JsonNode> jsonResponse = Unirest
                    .get(APPAUTH2_URL)
                    .header("authorization",authorization)
                    .asJson();
            JsonNode jsonData = jsonResponse.getBody();
            JSONObject jsonObject = jsonData.getObject();
            JSONObject data = jsonObject.getJSONObject("data");
            return data.getString("access_token");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnirestException ex) {
            Logger.getLogger(OAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
