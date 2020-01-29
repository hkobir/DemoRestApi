<?php
class DbOperations{
    private $con;
    function __construct(){
        require_once dirname(__FILE__) . '/DbConnect.php';
        $db = new DbConnect;
        $this->con = $db->connect();
    }
    /*  The Create Operation 
            The function will insert a new user in our database
        */
    public function createUser($email, $password, $name, $school){
        if(!$this->isEmailExist($email)){
            $stmt = $this->con->prepare("INSERT INTO users (email, password, name, school) VALUES (?, ?, ?, ?)");
            $stmt->bind_param("ssss", $email, $password, $name, $school);
            if($stmt->execute()){
                return USER_CREATED; 
            }else{
                return USER_FAILURE;
            }
        }
        return USER_EXISTS; 
    }

    public function userLogin($email, $password){
        if($this->isEmailExist($email)){
            $hashed_password = $this->getUsersPasswordByEmail($email);   //get pass from database
            if(password_verify($password, $hashed_password)){    //built in function to verify user pass and hashed pass
                return USER_AUTHENTICATED;
            }else{
                return USER_PASSWORD_NOT_MATCHED; 
            }
        }else{
            return USER_NOT_FOUND; 
        }
    }

    /*  
            The method is returning the password of a given user
            to verify the given password is correct or not
        */
    private function getUsersPasswordByEmail($email){
        $stmt = $this->con->prepare("SELECT password FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute(); 
        $stmt->bind_result($password);
        $stmt->fetch(); 
        return $password; 
    }

    /*
            The Read Operation
            Function is returning all the users from database
        */
    public function getAllUsers(){
        $stmt = $this->con->prepare("SELECT id, email, name, school FROM users;");
        $stmt->execute(); 
        $stmt->bind_result($id, $email, $name, $school);
        $users = array(); 
        while($stmt->fetch()){ 
            $user = array(); 
            $user['id'] = $id; 
            $user['email']=$email; 
            $user['name'] = $name; 
            $user['school'] = $school; 
            array_push($users, $user);
        }             
        return $users; 
    }

    /*
            The Read Operation
            This function reads a specified user from database
        */
    public function getUserByEmail($email){
        $stmt = $this->con->prepare("SELECT id, email, name, school FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute(); 
        $stmt->bind_result($id, $email, $name, $school);
        $stmt->fetch(); 
        $user = array(); 
        $user['id'] = $id; 
        $user['email']=$email; 
        $user['name'] = $name; 
        $user['school'] = $school; 
        return $user; 
    }

    /*
            The Update Operation
            The function will update an existing user
            from the database 
        */
    public function updateUser($email, $name, $school, $id){
        $stmt = $this->con->prepare("UPDATE users SET email = ?, name = ?, school = ? WHERE id = ?");
        $stmt->bind_param("sssi", $email, $name, $school, $id);
        if($stmt->execute())
            return true; 
        return false; 
    }


    /*
            The Delete Operation
            This function will delete the user from database
        */
    public function deleteUser($id){
        $stmt = $this->con->prepare("DELETE FROM users WHERE id = ?");
        $stmt->bind_param("i", $id);
        if($stmt->execute())
            return true; 
        return false; 
    }

    private function isEmailExist($email){
        $stmt = $this->con->prepare("SELECT id FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute(); 
        $stmt->store_result(); 
        return $stmt->num_rows > 0;  
    }

}
?>