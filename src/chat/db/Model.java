/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josepadilla
 */
public abstract class Model implements CRUDS {
    
    private HashMap<String, String> errors;
    
    private final Db db = new Db();

    @Override
    public boolean create(Object object) {
        handleError(DbUtils.validateFields(this.getClass(), object));
        return db.create(this.getClass(), object);
    }

    @Override
    public ArrayList<Object> getAll() {
        return db.selectAll(this.getClass());
    }

    @Override
    public Object getOne(Object pk) {
        handleError(DbUtils.validatePk(this.getClass(), pk));
        return db.getOneByPk(this.getClass(), pk);
    }

    @Override
    public boolean delete(Object pk) {
        handleError(DbUtils.validatePk(this.getClass(), pk));
        return db.delete(this.getClass(), pk);
    }

    @Override
    public ArrayList<Object> search(String field, Object object) {
        return db.search(this.getClass(), field, object);
    }
    
    private void handleError(HashMap<String, String> errors) {
        if(!errors.isEmpty()) {
            this.errors = new HashMap<>();
        } else {
            this.errors = errors;
        }
    }
    
    public HashMap<String, String> errors() {
        return this.errors;
    }
    
    @Override
    public String toString() {
        try {
            return DbUtils.getPkField(this.getClass()).get(this).toString();
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<?> rsToList (Class model, ResultSet rs) {
        return DbUtils.rsToArrayOfObjects(rs, model);
    }
    
}
