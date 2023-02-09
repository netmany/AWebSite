package com.next.era.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.boot.system.ApplicationHome;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
  
    @Value("${spring.datasource.url}")  
    private String ds_url;
 
    @Value("${spring.datasource.username}")
    private String username;
 
    @Value("${spring.datasource.password}")
    private String password;

    @Value("${upload.file.dir}")
    private String uploadDir;

    private String uploadFullPath = "";
    
    @GetMapping(value="/getAllUsers", produces ="application/json")
    @ResponseBody
    public String getAllUsers() {
        JSONObject res = new JSONObject();
        res.put("code", -1);

	String sql = "select * from user";
	try{
           JSONArray arr = new JSONArray();
	   
           // logger.info("getAllUsers/ db query begin...");
           
           Connection conn = DriverManager.getConnection(ds_url, username, password);
           Statement stmt = conn.createStatement();
	   ResultSet rs = stmt.executeQuery(sql);
           
   	   while (rs.next()) {
		JSONObject p = new JSONObject();
		p.put("name", rs.getString("name"));
                p.put("age", rs.getInt("age"));
                p.put("gender", rs.getInt("gender"));
                p.put("height", rs.getFloat("height"));
		p.put("country", rs.getString("country"));
                p.put("phonenum", rs.getLong("phonenum"));
                arr.add(p); 
	   } 

	   rs.close();
           stmt.close();
	   conn.close();
	   
           // logger.info("getAllUsers/ db query end.");

           res.put("code", 200);
           res.put("status", "OK");
           res.put("data", arr); 
           
	} catch (Exception e) {
		res.put("status", e.toString());
		logger.info("getAllUser/ db exception: ", e);
	}

        return res.toJSONString();
        
    }

    @PostMapping(value="/getSomeUser", produces ="application/json")
    @ResponseBody
    public String getSomeUser(@RequestBody JSONObject user) {
        JSONObject res = new JSONObject();
        res.put("code", -1);

	String sql = String.format("select * from user where name like '%%%s%%';", user.getString("name"));
	try{
           JSONArray arr = new JSONArray();
	   
           // logger.info("getSomeUser/ db query begin...");
           
           Connection conn = DriverManager.getConnection(ds_url, username, password);
           Statement stmt = conn.createStatement();
	   ResultSet rs = stmt.executeQuery(sql);
           
   	   while (rs.next()) {
		JSONObject p = new JSONObject();
		p.put("name", rs.getString("name"));
                p.put("age", rs.getInt("age"));
                p.put("gender", rs.getInt("gender"));
                p.put("height", rs.getFloat("height"));
		p.put("country", rs.getString("country"));
                p.put("phonenum", rs.getLong("phonenum"));
                arr.add(p); 
	   } 

	   rs.close();
           stmt.close();
	   conn.close();
	   
           // logger.info("getSomeUser/ db query end.");

           res.put("code", 200);
           res.put("status", "OK");
           res.put("data", arr); 
           
	} catch (Exception e) {
		res.put("status", e.toString());
		logger.info("getSomeUser/ db exception: ", e);
	}

        return res.toJSONString();
        
    }


    @RequestMapping(value="/addUser", method = RequestMethod.POST, produces ="application/json")
    @ResponseBody
    public  String addUser(@RequestBody JSONObject user){


	JSONObject res = new JSONObject();
        res.put("code", -1);        
 
        String sql = String.format("insert into user (name, age, gender, height, country, phonenum)"
                        + " values ('%s', '%d', '%d', '%f', '%s', '%d');", 
                        user.getString("name"), user.getInteger("age"), user.getInteger("gender"),
                        user.getFloat("height"), user.getString("country"), user.getLong("phonenum"));
 
	try{
           // logger.info("addUser/ db query begin...");
	   Connection conn = DriverManager.getConnection(ds_url, username, password);
           Statement stmt = conn.createStatement();
           int result = stmt.executeUpdate(sql); 
           if (result == 1) {
       	      res.put("code", 200);
              res.put("status", "OK");
           } else {
           	res.put("status", "db insert failed");
           }
  
           stmt.close();
	   conn.close();
	   // logger.info("addUser/ db query end.");
	} catch (Exception e) {
		res.put("status", e.toString());
		logger.info("addUser/ db exception: ", e);
	}

        return res.toJSONString();
 
        /* String data = "{}";
           JSONObject json = JSON.parseObject(data);

           String arr = "[]";
           JSONArray jsnArr = JSONArray.parseArray(arr);
           for (int i=0; i < jsnArr.size(); i++){
               String s = JSONObject.toJSONString(jsnArr.get(i)));
               System.out.println(s);
           }
         */
    }

    @PostMapping(value="/updateUser", produces ="application/json")
    @ResponseBody
    public  String updateUser(@RequestBody JSONObject user){


	JSONObject res = new JSONObject();
        res.put("code", -1);        
 
        String sql = String.format("update user set name = '%s', age = '%d', gender = '%d', height = '%f', country = '%s'"
                        + " where phonenum = '%d';",
                        user.getString("name"), user.getInteger("age"), user.getInteger("gender"),
                        user.getFloat("height"), user.getString("country"), user.getLong("phonenum"));
 
	try{
           // logger.info("updateUser/ db query begin...");
	   Connection conn = DriverManager.getConnection(ds_url, username, password);
           Statement stmt = conn.createStatement();
           int result = stmt.executeUpdate(sql); 
           if (result == 1) {
       	      res.put("code", 200);
              res.put("status", "OK");
           } else {
           	res.put("status", "db insert failed");
           }
  
           stmt.close();
	   conn.close();
	   // logger.info("updateUser/ db query end.");
	} catch (Exception e) {
		res.put("status", e.toString());
		logger.info("updateUser/ db exception: ", e);
	}

        return res.toJSONString();
 
   }

    @RequestMapping(value="/deleteUser", method = RequestMethod.POST, produces ="application/json")
    @ResponseBody
    public  String deleteUser(@RequestBody JSONObject user){


	JSONObject res = new JSONObject();
        res.put("code", -1);        
 
        String sql = String.format("delete from user where phonenum = %d ;", user.getLong("phonenum"));
 
	try{
           // logger.info("deleteUser/ db query begin...");
	   Connection conn = DriverManager.getConnection(ds_url, username, password);
           Statement stmt = conn.createStatement();
           int result = stmt.executeUpdate(sql); 
           if (result > 0) {
       	      res.put("code", 200);
              res.put("status", "OK");
           } else {
           	res.put("status", "db delete failed");
           }
  
           stmt.close();
	   conn.close();
	   // logger.info("deleteUser/ db query end.");
	} catch (Exception e) {
		res.put("status", e.toString());
		logger.info("deleteUser/ db exception: ", e);
	}

        return res.toJSONString();
    }

    @GetMapping(value="/listFiles", produces ="application/json")
    @ResponseBody
    public String listFiles() {
        JSONObject res = new JSONObject();
        res.put("code", -1);

        String dir = getUploadDir();
        File file = new File(dir);
	File[] fs = file.listFiles();
        JSONArray arr = new JSONArray();
	for(File f:fs){	
	   if(!f.isDirectory())
		arr.add(f.getName());
	}
        
        res.put("code", 200);
        res.put("status", "OK");
        res.put("data", arr); 
 
        return res.toJSONString();
        
    }

    @RequestMapping(value="/deleteFile", method = RequestMethod.POST, produces ="application/json")
    @ResponseBody
    public  String deleteFile(@RequestBody JSONObject fn){
	JSONObject res = new JSONObject();
        res.put("code", -1);        
 
        String filename = getUploadDir() + fn.getString("filename");
        File file = new File(filename);  
        if (file.isFile() && file.exists()) {  
            file.delete();
      	    res.put("code", 200);
            res.put("status", "OK");
        } else {
            res.put("status", "file delete failed");
        }

        return res.toJSONString();
    }

    private String getUploadDir(){
      synchronized(uploadFullPath) {
        if (uploadFullPath.equals("")) {
           ApplicationHome app = new ApplicationHome(getClass());
           File source = app.getSource();
           String filePath = source.getParentFile().toString() + "/" + uploadDir;
           File temp = new File(filePath);
           if (!temp.exists()){
              temp.mkdirs();
           }  
           uploadFullPath = filePath + "/";
        }
      }
      return uploadFullPath;
    }    

    @PostMapping(value="upLoadFiles")
    @ResponseBody
    public Object upLoadFiles(@RequestParam("myfiles") MultipartFile[] files) {
        for (MultipartFile f : files){
            String filename = f.getOriginalFilename();
            File localFile = new File(getUploadDir() + filename);
            try {
               f.transferTo(localFile);
               logger.info(filename + ": Uploaded Success");
            } catch (IOException e){
               logger.info("upLoadFiles/ " + filename, e);
               return "Upload Failed";
            }
        }
        return "upload ok";
    }

    private String urlEncode(String str) {
        String nstr = "";
        try {
           nstr = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
           logger.info("urlEncode", e);
        }
        return nstr;
    }


    @PostMapping(value="downloadFile")
    public void downloadFile(@RequestBody JSONObject fn, HttpServletResponse response) {
        String filename = getUploadDir() + fn.getString("filename");
        File file = new File(filename);  
        if (file.isFile() && file.exists()) {  
            response.setHeader("Content-Disposition", "attachment;filename=" 
                 + fn.getString("filename"));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/octet-stream");
            try{ 
               FileInputStream fis = new FileInputStream(file);
               OutputStream os = response.getOutputStream();
            
               int len = -1;
               byte[] buffer = new byte[1024];
               while((len = fis.read(buffer)) != -1) {
                  os.write(buffer, 0, len);
               } 
               os.flush();
               os.close();
               fis.close();
            } catch (IOException e) {
               logger.info("downloadFile exception ", e);
            }
        } else {
           response.setHeader("errorMsg", urlEncode("file not found!"));
           response.setStatus(404);
        }
    }

}
