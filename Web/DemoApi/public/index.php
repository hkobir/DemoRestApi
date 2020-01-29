<?php
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Factory\AppFactory;

require __DIR__ . '/../vendor/autoload.php';
//require __DIR__ . '/../includes/DbConnect.php';
require __DIR__ . '/../includes/DbOperations.php';

$app = AppFactory::create();

/*
//connection testing purpose
$app->get('/DemoApi/public/hello/{name}', function (Request $request, Response $response, array $args) {
	$name = $args['name'];
	$response->getBody()->write(" Hello, $name");
	$db = new DbConnect;
	if($db->connect()!=null){
		echo ' Connection Successfully!';
	}
	return $response;
});
*/



/*
endpoint: createuser
parameter: email,password,name,school
method: post

*/


//need to ensure $response->getBody()->write() in slim 4 for passing any message

$app->post('/DemoApi/public/createuser',function(Request $request, Response $response){
	if(!haveEmptyParameters(array('email', 'password', 'name', 'school'), $request, $response)){
		$request_data = $request->getParsedBody();

		$email = $request_data['email'];
		$password = $request_data['password'];
		$name = $request_data['name'];
		$school = $request_data['school'];

		$hash_password = password_hash($password,PASSWORD_DEFAULT);  //encrypt orginal passord to hash.Because we will not store user password
		$db = new DbOperations;

		$result = $db->createUser($email, $hash_password, $name, $school);   //passing for store to database

		if($result == USER_CREATED){   //successfully store
			$message = array();
			$message['error'] = false;
			$message['message'] = 'User created successfully!';
			$response->getBody()->write(json_encode($message));
			return $response
				->withHeader('Content-Type', 'application/json')
				->withStatus(201);   //resource created

		}
		else if($result == USER_FAILURE){

			$message = array();
			$message['error'] = true;
			$message['message'] = 'Some error occured';
			$response->getBody()->write(json_encode($message));
			return $response
				->withHeader('Content-type', 'application/json')
				->withStatus(422);   
		}
		else if($result == USER_EXISTS){
			$message = array();
			$message['error'] = true;
			$message['message'] = 'User already exists';
			$response->getBody()->write(json_encode($message));
			return $response
				->withHeader('Content-type', 'application/json')
				->withStatus(422);   
		}
	}

	return $response
		->withHeader('Content-type', 'application/json')
		->withStatus(422);   
});


/*
endpoint: userLogin
parameter: email,password
method: get

*/
$app->post('/DemoApi/public/userlogin', function(Request $request, Response $response){
	if(!haveEmptyParameters(array('email', 'password'), $request, $response)){
		$request_data = $request->getParsedBody(); 
		$email = $request_data['email'];
		$password = $request_data['password'];

		$db = new DbOperations; 
		$result = $db->userLogin($email, $password);
		if($result == USER_AUTHENTICATED){

			$user = $db->getUserByEmail($email);   //get user information when login success
			$response_data = array();
			$response_data['error']=false; 
			$response_data['message'] = 'Login Successful';
			$response_data['user']=$user; 
			$response->getBody()->write(json_encode($response_data));
			return $response
				->withHeader('Content-type', 'application/json')
				->withStatus(200);    
		}else if($result == USER_NOT_FOUND){
			$response_data = array();
			$response_data['error']=true; 
			$response_data['message'] = 'User not exist';
			$response->getBody()->write(json_encode($response_data));
			return $response
				->withHeader('Content-type', 'application/json')
				->withStatus(200);    
		}else if($result == USER_PASSWORD_NOT_MATCHED){
			$response_data = array();
			$response_data['error']=true; 
			$response_data['message'] = 'Invalid password';
			$response->getBody()->write(json_encode($response_data));
			return $response
				->withHeader('Content-type', 'application/json')
				->withStatus(200);  
		}
	}
	return $response
		->withHeader('Content-type', 'application/json')
		->withStatus(422);    
});

/*
endpoint: allusers
parameter: n/a
method: get

*/
$app->get('/DemoApi/public/allusers', function(Request $request, Response $response){
	$db = new DbOperations; 
	$users = $db->getAllUsers();
	$response_data = array();
	$response_data['error'] = false; 
	$response_data['users'] = $users; 
	$response->getBody()->write(json_encode($response_data));
	return $response
		->withHeader('Content-type', 'application/json')
		->withStatus(200);  
});


$app->put('/DemoApi/public/updateuser/{id}', function(Request $request, Response $response, array $args){
	$id = $args['id'];
	if(!haveEmptyParameters(array('email','name','school'), $request, $response)){
		$request_data = $request->getParsedBody(); 
		$email = $request_data['email'];
		$name = $request_data['name'];
		$school = $request_data['school']; 

		$db = new DbOperations; 
		if($db->updateUser($email, $name, $school, $id)){
			$response_data = array(); 
			$response_data['error'] = false; 
			$response_data['message'] = 'User Updated Successfully';
			$user = $db->getUserByEmail($email);
			$response_data['user'] = $user; 
			$response->getBody()->write(json_encode($response_data));
			return $response
				->withHeader('Content-type', 'application/json')
				->withStatus(200);  

		}else{
			$response_data = array(); 
			$response_data['error'] = true; 
			$response_data['message'] = 'Please try again later';
			$user = $db->getUserByEmail($email);
			$response_data['user'] = $user; 
			$response->getBody()->write(json_encode($response_data));
			return $response
				->withHeader('Content-type', 'application/json')
				->withStatus(200);  

		}
	}

	return $response
		->withHeader('Content-type', 'application/json')
		->withStatus(200);  
});

$app->delete('/DemoApi/public/deleteuser/{id}', function(Request $request, Response $response, array $args){
	$id = $args['id'];
	$db = new DbOperations; 
	$response_data = array();
	if($db->deleteUser($id)){
		$response_data['error'] = false; 
		$response_data['message'] = 'User has been deleted';    
	}else{
		$response_data['error'] = true; 
		$response_data['message'] = 'Plase try again later';
	}
	$response->getBody()->write(json_encode($response_data));
	return $response
		->withHeader('Content-type', 'application/json')
		->withStatus(200);
});

function haveEmptyParameters($required_params, $request, $response){
	$error = false; 
	$error_params = '';
	$request_params = $request->getParsedBody(); 
	foreach($required_params as $param){
		if(!isset($request_params[$param]) || strlen($request_params[$param])<=0){
			$error = true; 
			$error_params .= $param . ', ';
		}
	}
	if($error){
		$error_detail = array();
		$error_detail['error'] = true; 
		$error_detail['message'] = 'Required parameters ' . substr($error_params, 0, -2) . ' are missing or empty';
		$response->getBody()->write(json_encode($error_detail));   //need to ensure $response->getBody()->write() in slim 4
	}
	return $error; 

}
$app->run();

