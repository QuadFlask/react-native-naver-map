import React,{useEffect} from 'react';
import NaverMapView from "react-native-nmap";
import {PermissionsAndroid} from "react-native";

const App = () => {
    useEffect(async () => {
        await requestLocationPermission();
    });

    return <NaverMapView style={{width: '100%', height: '100%'}}
                         showsMyLocationButton={true}/>
};

async function requestLocationPermission() {
    try {
        const granted = await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
            {
                title: 'Location Permission',
                message: 'show my location need Location permission',
                buttonNeutral: 'Ask Me Later',
                buttonNegative: 'Cancel',
                buttonPositive: 'OK',
            },
        );
        if (granted === PermissionsAndroid.RESULTS.GRANTED) {
            console.log('You can use the camera');
        } else {
            console.log('Camera permission denied');
        }
    } catch (err) {
        console.warn(err);
    }
}


export default App;
