import 'package:chill/bloc/blocDelegate.dart';
import 'package:chill/repositories/userRepository.dart';
import 'package:chill/ui/pages/home.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

void main(){
WidgetsFlutterBinding.ensureInitialized();
BlocSupervisor.delegate=SimpleBlocDelegate();
runApp(Home(userRepository: UserRepository(),));
}

