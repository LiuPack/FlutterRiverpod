import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class $namePage extends ConsumerStatefulWidget {
  const $namePage({super.key});

  @override
  ConsumerState<ConsumerStatefulWidget> createState() => _$namePageState();
}

class _$namePageState extends ConsumerState<$namePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
    );
  }
}
