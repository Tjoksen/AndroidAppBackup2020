import 'package:flutter/material.dart';



void main() =>
    runApp(

        MaterialApp(
          title: "Long List",
          home:Scaffold(
            appBar: AppBar(title: Text("Long List"),),
            body: getListView(),
          ),

        )
    );

List<String> getLongListDataSource(){

var listedItems= List<String>.generate(1000, (counter)=>"RECORD $counter");
return listedItems;
}

getListView(){
  var listItems= getLongListDataSource();
  var listView=ListView.builder(
      itemBuilder: (context,index){
        return ListTile(
          leading: Icon(Icons.arrow_right),
          title: Text((listItems[index])),
          onTap: (){
            showSnackBar(context,listItems[index]);
          },
        );
      }
  );

  return listView;
}
void showSnackBar(BuildContext context,String item){
  var snackBar=SnackBar(
    content: Text("You tapped $item"),
    action: SnackBarAction(
      label:"UNDO",
      onPressed:(){
        debugPrint('Perfoming dummy UNDO operation.. ');
      },
    ),
  );

  Scaffold.of(context).showSnackBar(snackBar);

}