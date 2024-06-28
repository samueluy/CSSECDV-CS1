
package View;

import javax.swing.JOptionPane;

import Controller.SQLite;
import Model.Logs;

public class Login extends javax.swing.JPanel {

    private SQLite sql;
    public Frame frame;

    private int loginAttempts = 0; // counter
    private final int MAX_ATTEMPTS = 5; //Standard Lockout Limit
    
    public Login() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        usernameFld = new javax.swing.JTextField();
        passwordFld = new javax.swing.JPasswordField();
        registerBtn = new javax.swing.JButton();
        loginBtn = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SECURITY Svcs");
        jLabel1.setToolTipText("");

        usernameFld.setBackground(new java.awt.Color(240, 240, 240));
        usernameFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        usernameFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernameFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "USERNAME", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        passwordFld.setBackground(new java.awt.Color(240, 240, 240));
        passwordFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        passwordFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "PASSWORD", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        registerBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        registerBtn.setText("REGISTER");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });

        loginBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        loginBtn.setText("LOGIN");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(registerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(usernameFld)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordFld, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(usernameFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(126, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    //LOGIN LOGIC
    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        //frame.mainNav();

        if (isValidCredentials()) {
            validLogin();
        } else {
            invalidLogin("Invalid login credentials, please try again.");
        }
    }//GEN-LAST:event_loginBtnActionPerformed

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBtnActionPerformed
        frame.registerNav();
    }//GEN-LAST:event_registerBtnActionPerformed

    private boolean isValidCredentials() {
        return !fieldIsBlank() && !fieldIsInvalid();
    }

    private void validLogin() {
        SQLite sql = new SQLite();
        try {
            sql.addLogs(loginLog(usernameFld.getText(), "Logging in..."));
            handleUserLogin(sql);
        } catch (Exception e) {
            e.printStackTrace();  // Log or handle exception appropriately.
        }
    }

    private void handleUserLogin(SQLite sql) {
        String username = usernameFld.getText();
        if (sql.isUserExisting(username)) {
            if (sql.isUserLocked(username)) {
                sql.addLogs(loginLog(username, "Login attempt failed - Account is locked"));
                invalidLogin("Account locked due to too many failed login attempts. Please contact support.");
            } else {
                authenticateUser(sql, username);
            }
        } else {
            userNotFound(sql, username);
        }
    }

    private void authenticateUser(SQLite sql, String username) {
        if (sql.authenticateUser(username, getPasswordFromField())) {
            successfulLogin(sql, username);
        } else {
            failedLogin(sql, username);
        }
    }
    
    private void successfulLogin(SQLite sql, String username) {
        loginAttempts = 0;
        clearInputs();
        frame.mainNav();
    }
    
    private void failedLogin(SQLite sql, String username) {
        sql.addLogs(loginLog(username, "Failed attempt to login"));
        invalidLogin("Invalid login credentials, please try again.");
        incrementLoginAttempts(sql, username);
    }
    
    private void incrementLoginAttempts(SQLite sql, String username) {
        loginAttempts++;
        if (loginAttempts > MAX_ATTEMPTS) {
            lockUserAccount(sql, username);
        }
    }
    
    private void lockUserAccount(SQLite sql, String username) {
        if (sql.lockUser(username)) {
            sql.addLogs(loginLog(username, "Account locked - maximum attempts reached."));
            clearInputs();
            invalidLogin("Account locked due to too many failed login attempts. Please contact support.");
        }
    }
    
    private void userNotFound(SQLite sql, String username) {
        sql.addLogs(loginLog(username, "User not found."));
        invalidLogin("Invalid login credentials, please try again.");
    }

    private void clearInputs(){
        usernameFld.setText("");
        passwordFld.setText("");
    }
    
    private String getPasswordFromField(){
        return new String(passwordFld.getPassword());
    }
    
    private boolean fieldIsInvalid(){
        boolean usernameValid = !usernameFld.getText().trim().isEmpty();
        boolean passwordValid = getPasswordFromField().trim().length() > 0;
        
        return !(usernameValid && passwordValid);
    }
    
    private boolean fieldIsBlank(){
        if (usernameFld.getText().isBlank() || getPasswordFromField().isBlank())
            return true;
        return false;
    }

    private void invalidLogin(String message){
        JOptionPane.showMessageDialog(frame, message, "Message", JOptionPane.WARNING_MESSAGE);
        passwordFld.setText("");
    }

    private Logs loginLog(String username, String desc){
        return new Logs("LOGIN", username, desc);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPasswordField passwordFld;
    private javax.swing.JButton registerBtn;
    private javax.swing.JTextField usernameFld;
    // End of variables declaration//GEN-END:variables
}
