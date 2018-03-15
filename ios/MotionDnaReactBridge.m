//
//  MotionDnaManager.m
//  reactNativeHelloworld
//
//  Created by James Grantham on 3/14/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import "MotionDnaReactBridge.h"

@implementation MotionDnaReactBridge
RCT_EXPORT_MODULE();

- (instancetype)init {
  if (self = [super init]) {
    _motionDnaManager = [[MotionDnaManager alloc] init];
    _motionDnaManager.controller = self;
    [_motionDnaManager runMotionDna:@"4e7485cfe0c552a50112f33c573dca8c4e174786a59a6e407a589aa6d1d71d7a"];
    [_motionDnaManager setBinaryFileLoggingEnabled:YES];
    //[_motionDnaManager setLocationAndHeadingGPSMag];
    //[_motionDnaManager setLocationNavisens];
    [_motionDnaManager setLocalHeadingOffsetInDegrees:90];
    //    [_motionDnaManager setExternalPositioningState:LOW_ACCURACY];
    
    [_motionDnaManager setBackgroundModeEnabled:YES];
    [_motionDnaManager setCallbackUpdateRateInMs:100];
    [_motionDnaManager setNetworkUpdateRateInMs:100];
    [_motionDnaManager setExternalPositioningState:HIGH_ACCURACY];
  }
  return self;
}

- (NSArray<NSString *> *)supportedEvents
{
  return @[@"MotionDnaEvent"];
}

+ (BOOL)requiresMainQueueSetup
{
  return YES;
}

-(void)receiveMotionDna:(MotionDna*)motionDna {
  NSLog(@"Calling motionDNA");
//  -(Location)getLocation;
//  -(Location)getGPSLocation;
//  -(Attitude)getAttitude;
//  -(Motion)getMotion;
//  -(CalibrationStatus)getCalibrationStatus;
//  -(NSString*)getID;
//  -(NSString*)getDeviceName;
//  -(MotionStatistics)getMotionStatistics;
//  -(MotionStatistics)getPolygonMotionStatistics;
//  -(OrientationQuaternion)getQuaternion;
//  -(XY)getDebugVector;
  Location location = [motionDna getLocation];
  XYZ localLocation = location.localLocation;
  double heading = location.localHeading;
  Motion motion = [motionDna getMotion];
  
  NSString *motionDNAString = [NSString stringWithFormat:@"X:%.2f Y:%.2f Z:%.2f\nHeading:%.2f\nMotion: %@",localLocation.x,localLocation.y,localLocation.z,heading,[self NSStringFromMotionType:motion.motionType]];
//  NSLog(@"MotionDNA DATA\n%@",motionDNAString);
  NSDictionary *motionDnaDictionary = @{@"MotionDnaString": motionDNAString,
                                        @"location_localLocation_x":@([motionDna getLocation].localLocation.x),
                                        @"location_localLocation_y":@([motionDna getLocation].localLocation.y),
                                        @"location_localLocation_z":@([motionDna getLocation].localLocation.z),
                                        @"location_localHeading":@([motionDna getLocation].localHeading),
                                        @"motion_motionType":[self NSStringFromMotionType:[motionDna getMotion].motionType]
                                        };
  [self sendEventWithName:@"MotionDnaEvent" body:motionDnaDictionary];

}

- (NSString *)NSStringFromMotionType:(MotionType)mt {
  switch (mt) {
    case STATIONARY:
      return @"STATIONARY";
      break;
    case FIDGETING:
      return @"FIDGETING";
      break;
    case FORWARD:
      return @"FORWARD";
    default:
      break;
  }
  return nil;
}

RCT_EXPORT_METHOD(setMotionDnaCallback:(RCTResponseSenderBlock)callback)
{
  NSLog(@"Setting Callback");
  callback(@[[NSNull null],@"test"]);
//  _callback = callback;
}

@end
