import React, {useEffect} from 'react';
import NaverMapView, {Circle, Marker, Path, Polyline} from "react-native-nmap";
import {PermissionsAndroid} from "react-native";

const P0 = {latitude: 37.564362, longitude: 126.977011};
const P1 = {latitude: 37.565051, longitude: 126.978567};
const P2 = {latitude: 37.565383, longitude: 126.976292};

const App = () => {
    useEffect(() => {
        requestLocationPermission();
    }, []);

    return <NaverMapView style={{width: '100%', height: '100%'}}
                         showsMyLocationButton={true}>
        <Marker coordinate={P0}/>
        <Marker coordinate={P1} pinColor="blue"/>
        <Marker coordinate={P2} pinColor="red"/>
        <Path coordinates={[P0, P1]}/>
        <Polyline coordinates={[P1, P2]}/>
        <Circle coordinate={P0} color={"rgba(255,0,0,0.3)"} radius={200}/>
    </NaverMapView>
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
            console.log('You can use the location');
        } else {
            console.log('Camera permission denied');
        }
    } catch (err) {
        console.warn(err);
    }
}


export default App;
