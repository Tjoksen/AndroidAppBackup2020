import 'package:flutter/material.dart';
import 'Authentication.dart';

class LoginRegisterPage extends StatefulWidget{
  LoginRegisterPage({
    this.auth,
    this.onSignedIn,
});
  final AuthImplementation auth;
  final VoidCallback onSignedIn;


  @override
  State<StatefulWidget> createState() {
    return _LoginRegisterPageState();
  }

}
enum FormType{
  login,
  register
}

class _LoginRegisterPageState extends State<LoginRegisterPage>{
  final formKey=new GlobalKey<FormState>();
  FormType _formType=FormType.login;
  String _email="";
  String _password="";

  //Methods
  //validate user input and save data
   bool validateAndSave() {
      final form=formKey.currentState;
      if(form.validate()){
        form.save();
        return true;
      }
      else{
        return false;
      }

   }//end of validateAndSave

  //validate user input and submit
  void validateAndSubmit() async{
     if(validateAndSave()){
       try{
          if(_formType==FormType.login){
              String userId=await widget.auth.SignIn(_email, _password);
              print("Login userid: " +userId );
          }
          else{
            String userId=await widget.auth.SignUp(_email, _password);
            print("Registered userid: " +userId );
          }
          widget.onSignedIn();
       }
       catch(e){
            print("Error: "+e.toString());
       }
     }

  }//end of validateAndSubmit

   //send user to registration
   void sendToRegister(){
      formKey.currentState.reset();
      setState(() {
        _formType=FormType.register;
      });
   }
  //send user to registration
  void sendToLogin(){
    formKey.currentState.reset();
    setState(() {
      _formType=FormType.login;
    });
  }


  //Degisn
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Login/Register"),
      ),
      body: new Container(
        margin: EdgeInsets.all(15.0),
        child: new Form(
          key: formKey,
            child: new Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: createInputs()+createButtons(),
            )

        ),
      ),
    );
  }

  List<Widget> createInputs(){
    return[
      SizedBox(height: 10.0,),
     logo(),
      SizedBox(height: 20.0,),
      new TextFormField(
        decoration:new InputDecoration(labelText: 'Email') ,
        validator: (value){
          return value.isEmpty?'Email is required...!':null;
        },
        onSaved: (value){
          _email=value;
        },
      ),
      SizedBox(height: 10.0,),
      new TextFormField(
        decoration:new InputDecoration(labelText: 'Password') ,
        obscureText: true,
        validator: (value){
          return value.isEmpty?'Password is required...!':null;
        },
        onSaved: (value){
          _password=value;
        },
      ),
      SizedBox(height: 10.0,),
    ];
  }
 Widget logo(){
    return new Hero(
      tag: 'hero',
        child: new CircleAvatar(
          backgroundColor: Colors.transparent,
          radius: 50.0,
          child: Image.asset('images/logo.png'),
        )
    );
 }

  List<Widget> createButtons(){
   if(_formType==FormType.login){
     return[
           new  RaisedButton(
             child:new Text("Login",style: new TextStyle(fontSize: 20.0),),
             textColor: Colors.white,
             color: Colors.green,
             onPressed: validateAndSubmit,

           ),
           new  FlatButton(
             child:new Text("Create New Account here",style: new TextStyle(fontSize: 14.0),),
             textColor: Colors.black,
             onPressed: sendToRegister,

           )
     ];
   }
   else{
     return[
       new  RaisedButton(
         child:new Text("Create Account",style: new TextStyle(fontSize: 20.0),),
         textColor: Colors.white,
         color: Colors.green,
         onPressed: validateAndSubmit,

       ),
       new  FlatButton(
         child:new Text("Already have an Account?Login here",style: new TextStyle(fontSize: 14.0),),
         textColor: Colors.black,
         onPressed: sendToLogin,

       )
     ];
   }
  }

}