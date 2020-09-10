import 'package:flutter/material.dart';
import 'Authentication.dart';
class HomePage extends StatefulWidget{
  HomePage({
      this.auth,
      this.onSignedOut,
});
final AuthImplementation auth;
final VoidCallback onSignedOut;

  @override
  State<StatefulWidget> createState() {
    return _HomePageState();
  }

}
class _HomePageState extends State<HomePage>{
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("HomePage"),
      ),
      body: new Container(

      ),
      bottomNavigationBar: new BottomAppBar(
        color: Colors.green,
        child: new Container(
          margin:const EdgeInsets.only(left: 100.0,right: 100.0),
          child: new Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            mainAxisSize: MainAxisSize.max,
            children: <Widget>[
             new IconButton(
                 icon: new Icon(Icons.local_car_wash),
                 iconSize: 30,
                 color: Colors.white,
               onPressed: _logoutUser,
             ),
              new IconButton(
                  icon: new Icon(Icons.add_a_photo),
                  iconSize: 30,
                  color: Colors.white,
                onPressed: null,
              )
            ],

          ),
        ),
      ),
    );
  }


  void _logoutUser() async {
    try{
        await widget.auth.SignOut();
        widget.onSignedOut();
    }
    catch(e){
      print("Error : " + e.toString());
    }
  }
}