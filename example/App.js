import 'react-native-gesture-handler';
import React, {useEffect} from 'react';
import NaverMapView, {Align, Circle, Marker, Path, Polygon, Polyline} from "./map";
import {Image, ImageBackground, PermissionsAndroid, Platform, ScrollView, Text, TouchableOpacity, View} from "react-native";
import {NavigationContainer} from '@react-navigation/native';
import {createBottomTabNavigator} from "@react-navigation/bottom-tabs";
import {createStackNavigator} from "@react-navigation/stack";

const P0 = {latitude: 37.564362, longitude: 126.977011};
const P1 = {latitude: 37.565051, longitude: 126.978567};
const P2 = {latitude: 37.565383, longitude: 126.976292};
const P4 = {latitude: 37.564834, longitude: 126.977218};
const P5 = {latitude: 37.562834, longitude: 126.976218};

const Tab = createBottomTabNavigator();
const Stack = createStackNavigator();

const App = () => {
    return <NavigationContainer>
        <Stack.Navigator>
            <Stack.Screen name="home" component={HomeScreen}/>
            <Stack.Screen name="stack" component={MapViewScreen2}/>
        </Stack.Navigator>
    </NavigationContainer>
}

const HomeScreen = () =>
    <Tab.Navigator>
        <Tab.Screen name={"map"} component={MapViewScreen}/>
        <Tab.Screen name={"text"} component={TextScreen}/>
    </Tab.Navigator>

const TextScreen = () => {
    return <Text>text</Text>
}

const MapViewScreen = ({navigation}) => {
    useEffect(() => {
        requestLocationPermission();
    }, []);

    return <>
        <NaverMapView style={{width: '100%', height: '100%'}}
                      showsMyLocationButton={true}
                      center={{...P0, zoom: 16}}
                      onTouch={e => console.warn('onTouch', JSON.stringify(e.nativeEvent))}
                      onCameraChange={e => console.warn('onCameraChange', JSON.stringify(e))}
                      onMapClick={e => console.warn('onMapClick', JSON.stringify(e))}
                      useTextureView>
            <Marker coordinate={P0} onClick={() => console.warn('onClick! p0')} caption={{text: "test caption", align: Align.Left}}/>
            <Marker coordinate={P1} pinColor="blue" onClick={() => console.warn('onClick! p1')}/>
            <Marker coordinate={P2} pinColor="red" alpha={0.5} onClick={() => console.warn('onClick! p2')}/>
            <Marker coordinate={P4} onClick={() => console.warn('onClick! p4')} image={require("./marker.png")} width={48} height={48}/>
            <Path coordinates={[P0, P1]} onClick={() => console.warn('onClick! path')} width={10}/>
            <Polyline coordinates={[P1, P2]} onClick={() => console.warn('onClick! polyline')}/>
            <Circle coordinate={P0} color={"rgba(255,0,0,0.3)"} radius={200} onClick={() => console.warn('onClick! circle')}/>
            <Polygon coordinates={[P0, P1, P2]} color={`rgba(0, 0, 0, 0.5)`} onClick={() => console.warn('onClick! polygon')}/>
            <Marker coordinate={P5} onClick={() => console.warn('onClick! p0')} width={96} height={96}>
                <View style={{backgroundColor: 'rgba(255,0,0,0.2)', borderRadius: 80}}>
                    <View style={{backgroundColor: 'rgba(0,0,255,0.3)', borderWidth: 2, borderColor: 'black', flexDirection: 'row'}}>
                        <Image source={require("./marker.png")} style={{
                            width: 32, height: 32,
                            backgroundColor: 'rgba(0,0,0,0.2)', resizeMode: 'stretch',
                            borderWidth: 2, borderColor: 'black'
                        }} fadeDuration={0}/>
                        <Text>Image</Text>
                    </View>
                    <ImageBackground source={require("./marker.png")} style={{width: 64, height: 64}}>
                        <Text>image background</Text>
                    </ImageBackground>
                </View>
            </Marker>
        </NaverMapView>
        <TouchableOpacity style={{position: 'absolute', bottom: '10%', right: 8}} onPress={() => navigation.navigate('stack')}>
            <View style={{backgroundColor: 'gray', padding: 4}}>
                <Text style={{color: 'white'}}>open stack</Text>
            </View>
        </TouchableOpacity>
        <Text style={{position: 'absolute', top: '95%', width: '100%', textAlign: 'center'}}>Icon made by Pixel perfect from www.flaticon.com</Text>
    </>
};

const MapViewScreen2 = ({navigation}) => {
    return <View>
        <TouchableOpacity onPress={navigation.goBack}>
            <View style={{backgroundColor: 'gray', padding: 4}}>
                <Text style={{color: 'white'}}>goBack</Text>
            </View>
        </TouchableOpacity>
        <ScrollView style={{width: '100%', height: '100%'}}>
            <Text>scrollGesturesEnabled: default</Text>
            <NaverMapView style={{width: '100%', height: 200}}
                          center={{...P0, zoom: 15}}
                          useTextureView>
                <Marker coordinate={P0}/>
            </NaverMapView>
            {Array.from({length: 10}, (_, i) => i).map(i => <Text key={i}></Text>)}
            <Text>scrollGesturesEnabled</Text>
            <NaverMapView style={{width: '100%', height: 200}}
                          center={{...P0, zoom: 15}}
                          scrollGesturesEnabled
                          useTextureView>
                <Marker coordinate={P0}/>
            </NaverMapView>
            {Array.from({length: 10}, (_, i) => i).map(i => <Text key={i}></Text>)}
            <Text>scrollGesturesEnabled: false</Text>
            <NaverMapView style={{width: '100%', height: 200}}
                          center={{...P0, zoom: 15}}
                          scrollGesturesEnabled={false}
                          useTextureView>
                <Marker coordinate={P0}/>
            </NaverMapView>
            {Array.from({length: 10}, (_, i) => i).map(i => <Text key={i}></Text>)}
        </ScrollView>
    </View>
}

async function requestLocationPermission() {
    if (Platform.OS !== 'android') return;
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
            console.log('Location permission denied');
        }
    } catch (err) {
        console.warn(err);
    }
}


export default App;
