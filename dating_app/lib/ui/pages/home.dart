import 'package:chill/bloc/authentication/authentication_bloc.dart';
import 'package:chill/bloc/authentication/authentication_state.dart';
import 'package:chill/repositories/userRepository.dart';
import 'package:chill/ui/pages/login.dart';
import 'package:chill/ui/pages/signUp.dart';


import 'package:chill/ui/pages/splash.dart';
import 'package:chill/ui/widgets/tabs.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';


class Home extends StatelessWidget {
  final UserRepository _userRepository;
   AuthenticationBloc _authenticationBloc;
  Home({@required UserRepository userRepository})
      : assert(userRepository != null),
        _userRepository = userRepository;

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context)=>_authenticationBloc,
      child: MaterialApp(
      debugShowCheckedModeBanner: false,
      home: BlocBuilder(
        bloc: _authenticationBloc,
        builder: (BuildContext context,AuthenticationState state) {
          if (state is Unitialized) {
            return Splash();
          }
          else  return Login(userRepository: _userRepository,);

        },
      ),
      ),
    );
  }
}



