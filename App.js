/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {NativeEventEmitter, NativeModules} from 'react-native';

// module.exports = NativeModules.MotionDnaReactBridge;
import {
  Platform,
  StyleSheet,
  Text,
  TextInput,
  View,
  PermissionsAndroid
} from 'react-native';

motionDNAstring = ""

// function motionDNACallback(err, parameter) {
    
//     motionDNAstring = parameter
// }

async function requestNavisensPermission() {
  try {
    var granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
      {
        'title': 'Cool Photo App Camera Permission',
        'message': 'Cool Photo App needs access to your camera ' +
                   'so you can take awesome pictures.'
      }
    )
    var granted1 = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION,
      {
        'title': 'Cool Photo App Camera Permission',
        'message': 'Cool Photo App needs access to your camera ' +
                   'so you can take awesome pictures.'
      }
    )
    var granted2 = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
      {
        'title': 'Cool Photo App Camera Permission',
        'message': 'Cool Photo App needs access to your camera ' +
                   'so you can take awesome pictures.'
      }
    )
  } catch (err) {
    console.warn(err)
  }
}

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {
    constructor() {
        super();
        requestNavisensPermission();
        this.state = {
            motionDNAstring: "test",
            location_localLocation_x: "x",
            location_localLocation_z: "y",
            location_localLocation_y: "z",
            location_localHeading: "heading",
            motion_motionType: "nada"
        };
        console.log("initialized react app")

        this.motionManager = NativeModules.MotionDnaReactBridge
        this.motionManager.runMotionDna("<ENTER DEV KEY HERE>")
        this.motionManager.setBinaryFileLoggingEnabled(true)
        this.motionManager.setLocalHeadingOffsetInDegrees(90)
        this.motionManager.setExternalPositioningState("HIGH_ACCURACY")
        this.motionManager.setPowerMode("PERFORMANCE");
        this.motionManager.setCallbackUpdateRateInMs(100)
        this.motionManager.setNetworkUpdateRateInMs(100)

        this.motionDnaEmitter = new NativeEventEmitter(this.motionManager);
        console.log("set emitter")

        this.subscription = this.motionDnaEmitter.addListener(
            'MotionDnaEvent',
            (motionDna) => {
                console.log("parameter: " + motionDna.location_localHeading);
                this.setState({motionDNAstring: motionDna.MotionDnaString,
                    location_localLocation_x: motionDna.location_localLocation_x.toString(),
                    location_localLocation_y: motionDna.location_localLocation_y.toString(),
                    location_localLocation_z: motionDna.location_localLocation_z.toString(),
                    location_localHeading: motionDna.location_localHeading.toString(),
                    motion_motionType: motionDna.motion_motionType
                        });
                // this.setState({[motionDna.target.id]:motionDna.target.value});
            });

          this.errorSubscription = this.motionDnaEmitter.addListener('MotionDnaErrorEvent',(error) => {
            this.setState({
              errorCode: error.errorCode,
              errorString: error.errorString 
            });
          });
        console.log("done initializing")
        
        // this.motionManager.setMotionDnaCallback((err, parameter) => 
    }
    
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Test!
        </Text>
        <TextInput style={styles.instructions}
        value={this.state.motionDNAstring}
        />
        <TextInput style={styles.instructions}
        value={this.state.location_localLocation_x}
        />
        <TextInput style={styles.instructions}
        value={this.state.location_localLocation_z}
        />
        <TextInput style={styles.instructions}
        value={this.state.location_localLocation_y}
        />
        <TextInput style={styles.instructions}
        value={this.state.location_localHeading}
        />
        <TextInput style={styles.instructions}
        value={this.state.motion_motionType}
        />
        <Text style={styles.instructions}>
          {instructions}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#dd6260',
    marginBottom: 5,
  },
});
