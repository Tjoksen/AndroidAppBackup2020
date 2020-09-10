import 'package:equatable/equatable.dart';


abstract class AuthenticationState extends Equatable{
  const AuthenticationState();
  @override
  List<Object> get props => [];
}

class Unitialized extends AuthenticationState {}
class Authenticated extends AuthenticationState {
  final String userId;

  Authenticated(this.userId);
  @override
  List<Object> get props => [userId];

  @override
  String toString() => "Authenticated $userId";
}
class AuthenticatedButNotSet extends AuthenticationState{
  final String userId;

  AuthenticatedButNotSet(this.userId);
  @override
  List<Object> get props => [userId];
  @override
  String toString() => "Authenticated but not set $userId";
}
class Unauthenticated  extends AuthenticationState {}
