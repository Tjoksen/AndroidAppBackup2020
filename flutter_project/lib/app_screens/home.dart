import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Home extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
        child: Container(
      alignment: Alignment.center,
      color: Colors.deepPurple,
      padding: EdgeInsets.only(left: 10.0, top: 40.0),
      child: Column(
        children: <Widget>[
          Row(
            children: <Widget>[
              Expanded(
                  child: Text(
                "Boeng 707 :",
                textDirection: TextDirection.ltr,
                style: TextStyle(
                    decoration: TextDecoration.none,
                    fontSize: 20.0,
                    fontFamily: 'Bellerose',
                    fontWeight: FontWeight.w700,
                    fontStyle: FontStyle.italic),
              )),
              Expanded(
                  child: Text(
                "Harare to Joburg",
                textDirection: TextDirection.ltr,
                style: TextStyle(
                    decoration: TextDecoration.none,
                    fontSize: 20.0,
                    fontFamily: 'Bellerose',
                    fontWeight: FontWeight.w700,
                    fontStyle: FontStyle.italic),
              )),
            ],
          ),
          Row(
            children: <Widget>[
              Expanded(
                  child: Text(
                "Ethopian Airways :  ",
                textDirection: TextDirection.ltr,
                style: TextStyle(
                    decoration: TextDecoration.none,
                    fontSize: 20.0,
                    fontFamily: 'Bellerose',
                    fontWeight: FontWeight.w700,
                    fontStyle: FontStyle.italic),
              )),
              Expanded(
                  child: Text(
                "Kenya to Japan",
                textDirection: TextDirection.ltr,
                style: TextStyle(
                    decoration: TextDecoration.none,
                    fontSize: 20.0,
                    fontFamily: 'Bellerose',
                    fontWeight: FontWeight.w700,
                    fontStyle: FontStyle.italic),
              )),
            ],
          ),
          JscImageLoader()
          ,
          FlightBookingButton()
        ],
      ),
    ));
  }
}

class JscImageLoader extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
   AssetImage assetImage= new AssetImage('images/jsc.jpg');
   Image image=new Image(image: assetImage,width: 250.0,height: 250.0,);

    return Container(
      padding: EdgeInsets.all(20.0),
      child: image,
    
    );
  }

}
class FlightBookingButton extends StatelessWidget{
  @override
  Widget build(BuildContext context) {

    return Container(
      margin: EdgeInsets.only(top:30.0),
      width: 250,
      height: 50,
      child: RaisedButton(
        color: Colors.black,
        child: Text(
          "Book Flight",
          style: TextStyle(
            color: Colors.white,
            fontFamily:'Bellerose',
          ),
        ),
        elevation:6.0 ,
        onPressed:(){
          bookFlight(context);
        },
      ),
    );
  }
  void bookFlight(BuildContext context){
    var alertDialog  = AlertDialog(
      title: Text("Text Booked Successfully"),
      content: Text("Have a pleasant flight"),
    );
    
    showDialog(
      context: context,
      builder:(BuildContext context)=> alertDialog


    );
  }

}
