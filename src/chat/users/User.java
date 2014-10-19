package chat.users;

import chat.db.DbField;
import chat.db.Model;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import chat.services.ServiceConstants;
import java.sql.Connection;
import java.sql.ResultSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author josepadilla
 */
public class User extends Model {
    
    @DbField(type="pk")
    public String username;
    
    @DbField(type="field")
    public String password;
    
    private Boolean exists = false;
    
    public User() {}
    
    public User (String username, String password) {
        this.username = username;
        this.password = password;
        
        if (getOne(this.username) != null) {
            exists = true;
        }
    }
    
    public Boolean exists() {
        return this.exists;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public boolean signIn() {
        return this.exists && checkPassword();
    }

    public boolean signUp() {
        if (username == null || password == null) {
            return false;
        } else return !this.exists() && createUser();
    }

    private boolean checkPassword() {
        try {
            Class.forName(ServiceConstants.SQLITE_CLASS_NAME);
            
            Connection conn = DriverManager.getConnection(ServiceConstants.CONNECTION_STRING);
            
            String sql = "SELECT username FROM USER WHERE username = '" 
                    + this.username +"' AND password = '" + this.password + "';";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            if (rs.next()) {
                return true;
            }
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
        return false;
    }

    private boolean createUser() {
        return create(this);
    }
    
}