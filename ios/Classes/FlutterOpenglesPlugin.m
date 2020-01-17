#import "FlutterOpenglesPlugin.h"
#if __has_include(<flutter_opengles/flutter_opengles-Swift.h>)
#import <flutter_opengles/flutter_opengles-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_opengles-Swift.h"
#endif

@implementation FlutterOpenglesPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterOpenglesPlugin registerWithRegistrar:registrar];
}
@end
