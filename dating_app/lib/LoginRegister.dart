import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';


class LoginRegister extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return _LoginRegisterState();
  }

}

enum FormType{
  login,
  register
}

class _LoginRegisterState extends State<LoginRegister>{
  final formKey=GlobalKey<FormState>();
  FormType _formType=FormType.login;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: new Text("Login/Register Page"),
      ),
      body: Container(
        margin: EdgeInsets.all(15.0),
        child: Form(
          key: formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
          children: createInputs()+createButons(),
          ),
        ),
      ),
    );
  }
  List<Widget>  createInputs(){
    return[
      SizedBox(height: 10.0,),
        logo(),
      SizedBox(height: 20.0,),
      TextFormField(
        decoration: InputDecoration(labelText: "Email"),
      ),
      SizedBox(height: 10.0,),
      TextFormField(
        decoration: InputDecoration(labelText: "Password"),
      ),
      SizedBox(height: 20.0,),

    ];
  }


  Widget logo(){
    return Hero(
      tag: "Dp",
    child: CircleAvatar(
      backgroundColor: Colors.transparent,
      radius: 110,
      child: Image.asset('assets/online.jpg'),
    ),
    );
  }
  List<Widget>  createButons(){
    return[

      RaisedButton(
        child: Text("Login",style: TextStyle(fontSize: 20.0)),
        textColor: Colors.white,
        color: Colors.pink,
        onPressed: validateAndSave,
      ),
      SizedBox(height: 10.0,),
      FlatButton(
          child: Text("Doesn't have an account?Click here",style: TextStyle(fontSize: 14.0)),
        textColor: Colors.pink,
        onPressed: registerUser,
      ),
      SizedBox(height: 20.0,),

    ];
  }
//methods
  validateAndSave() {

  }

  registerUser() {
formKey.currentState.reset();
setState(() {
  _formType=FormType.register;
});
  }

  loginUser() {
    formKey.currentState.reset();
    setState(() {
      _formType=FormType.login;
    });
  }

}