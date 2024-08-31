import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'state.dart';

final $nameProvider = StateNotifierProvider.autoDispose<$nameNotifier,$nameState>((ref) {
  return $nameNotifier();
});


class $nameNotifier extends StateNotifier<$nameState> {
  $nameNotifier() : super(const $nameState());

}
