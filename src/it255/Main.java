package it255;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Mihajlo Nesic 3348
 */
public class Main {

    private final String API_URL = "http://89.216.56.107:8080/restfull/rest";
    private final String API_USERS = API_URL + "/users";
    
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        System.out.println("-GET");
        
        User userGet = get(393L);
        System.out.println(userGet);
        
        System.out.println("\n-POST");
        
        User userPost = new User();
        userPost.setFullName("Test Test");
        userPost.setEmail("test@test.com");
        userPost.setPassword("password123");
        userPost.setEmailConfirmed(true);
        userPost.setEmailConfirmationCode("999999");
        
        post(userPost);
    }
    
    private User get(Long id) {
        try {
            URL url = new URL(API_USERS+"/"+id);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error: " + conn.getResponseCode());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            
            String json = "";
            String output;
            
            while ((output = br.readLine()) != null) {
                json += output;
            }
            
            conn.disconnect();
            
            Gson gson = new Gson();
            User user = gson.fromJson(json, new TypeToken<User>(){}.getType());
            
            return user;
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    
    private void post(User u) {
        String userJson = new Gson().toJson(u);
        
        try {
            URL url = new URL(API_USERS);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            OutputStream os = conn.getOutputStream();
            os.write(userJson.getBytes("UTF-8"));
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error: " + conn.getResponseCode());
            }
            
            conn.disconnect();
            
            System.out.println("User posted successfully!");
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}