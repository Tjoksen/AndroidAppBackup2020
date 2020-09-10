import 'package:flutter/material.dart';
import 'LoginRegisterPage.dart';
import 'HomePage.dart';
import 'Authentication.dart';
class MappingPage extends StatefulWidget{

  final AuthImplementation auth;

    MappingPage({
    this.auth,
});

  @override
  State<StatefulWidget> createState() {
    return _MappingPageState();
  }

}
enum AuthStatus{
  notSigned,
  signedIn,
}

class _MappingPageState extends State<MappingPage>{
  AuthStatus authStatus=AuthStatus.notSigned;

  @override
  void initState() {
    super.initState();
    widget.auth.getCurrentUser().then((firebaseUserId){
        setState(() {
          authStatus=firebaseUserId==null?AuthStatus.notSigned:AuthStatus.signedIn;
         // 15:20
        });
    });
  }

  void _signedIn(){
    setState(() {
      authStatus=AuthStatus.signedIn;
    });
  }
  void _signedOut(){
    setState(() {
      authStatus=AuthStatus.notSigned;
    });
  }
  @override
  Widget build(BuildContext context) {
    switch(authStatus){
      case AuthStatus.notSigned:
        return new LoginRegisterPage(
          auth:widget.auth,
          onSignedIn:_signedIn,
        );
      case AuthStatus.signedIn:
          return new HomePage(
          auth:widget.auth,
          onSignedOut:_signedOut,
        );

    }
    return null;
  }

}