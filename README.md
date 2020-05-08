react-native-naver-map [![npm version](https://badge.fury.io/js/react-native-nmap.svg)](https://badge.fury.io/js/react-native-nmap)
-----

네이버맵의 리액트 네이티브 브릿지입니다.

![](https://raw.githubusercontent.com/QuadFlask/react-native-naver-map/master/example/screenshot/screenshot.png)


## 설치

```
npm install react-native-nmap --save;
```

- **React Native 0.60+**

```bash
$ cd ios/ && pod install
```

- **React Native <= 0.59**

```bash
$ react-native link react-native-nmap
$ cd ios/ && pod install
```

> ios의 경우 `git-lfs` 설치가 필요합니다. [참고](https://github.com/navermaps/ios-map-sdk/issues/1#issuecomment-484007817)


### 안드로이드 추가 설정

[네이버 맵 안드로이드 SDK 문서](https://navermaps.github.io/android-map-sdk/guide-ko/1.html)를 따라 API키와 레포지터리 경로를 추가합니다

`/android/build.gradle` 파일에 아래와 같이 레포지터리를 추가합니다
```
allprojects {
    repositories {
        google()
        jcenter()

        // 네이버 지도 저장소
        maven {
            url 'https://navercorp.bintray.com/maps'
        }
    }
}
```

`/android/app/src/AndroidManifest.xml`에 아래와 같이 추가하고 발급받은 클라이언트 아이디로 바꿔줍니다.
```xml
<manifest>
    <application>
        <meta-data
            android:name="com.naver.maps.map.CLIENT_ID"
            android:value="YOUR_CLIENT_ID_HERE" />
    </application>
</manifest>
```

### IOS 추가 설정

[네이버 맵 IOS SDK 문서](https://navermaps.github.io/ios-map-sdk/guide-ko/1.html)를 따라 API키와 레포지터리 경로를 추가합니다.

`info.plist`에 아래와 같이 발급받은 클라이언트 아이디를 추가해줍니다.

![image](https://user-images.githubusercontent.com/49827449/66392740-b2fd5f00-ea0b-11e9-8c38-23e604b1009d.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
...
    <key>NMFClientId</key>
    <string>YOUR_CLIENT_ID_HERE</string>
...
<dict>
<plist>
```

#### ⚠️ 주의

현재 iOS pod 설정이 제대로 되어있지 않아 링크가 되지 않습니다. 

현재로선 ios 폴더의 *.m, *.h 파일을 프로젝트에 복사 하신뒤 사용하실 수 있습니다.


## 예제

> [example/App.js](https://github.com/QuadFlask/react-native-naver-map/blob/master/example/App.js) 

```tsx
import NaverMapView, {Circle, Marker, Path, Polyline, Polygon} from "react-native-nmap";

function MyMap() {
    const P0 = {latitude: 37.564362, longitude: 126.977011};
    const P1 = {latitude: 37.565051, longitude: 126.978567};
    const P2 = {latitude: 37.565383, longitude: 126.976292};

    return <NaverMapView style={{width: '100%', height: '100%'}}
                         showsMyLocationButton={true}
                         center={{...P0, zoom: 16}}
                         onTouch={e => console.warn('onTouch', JSON.stringify(e.nativeEvent))}
                         onCameraChange={e => console.warn('onCameraChange', JSON.stringify(e))}
                         onMapClick={e => console.warn('onMapClick', JSON.stringify(e))}>
        <Marker coordinate={P0} onClick={() => console.warn('onClick! p0')}/>
        <Marker coordinate={P1} pinColor="blue" onClick={() => console.warn('onClick! p1')}/>
        <Marker coordinate={P2} pinColor="red" onClick={() => console.warn('onClick! p2')}/>
        <Path coordinates={[P0, P1]} onClick={() => console.warn('onClick! path')} width={10}/>
        <Polyline coordinates={[P1, P2]} onClick={() => console.warn('onClick! polyline')}/>
        <Circle coordinate={P0} color={"rgba(255,0,0,0.3)"} radius={200} onClick={() => console.warn('onClick! circle')}/>
        <Polygon coordinates={[P0, P1, P2]} color={`rgba(0, 0, 0, 0.5)`} onClick={() => console.warn('onClick! polygon')}/>
    </NaverMapView>
}
```


## 컴포넌트

타입스크립트 타입 정의가 포함되어 있어 타입스크립트 사용을 추천합니다.

### 기본 타입

```ts
export interface Coord {
    latitude: number;
    longitude: number;
}
export interface Region extends Coord {
    latitudeDelta: number;
    longitudeDelta: number;
}
export interface Rect {
    left?: number;
    top?: number;
    right?: number;
    bottom?: number;
}
```

### `NaverMapView`
```ts
interface NaverMapViewProps {
    center?: Coord & {
        zoom?: number;
        tilt?: number;
        bearing?: number;
    };
    tilt?: number;
    bearing?: number;
    mapPadding?: Rect;
    logoMargin?: Rect;
    logoGravity?: Gravity;
    onInitialized?: Function;
    onCameraChange?: (event: {
        latitude: number;
        longitude: number;
        zoom: number;
    }) => void;
    onMapClick?: (event: {
        x: number;
        y: number;
        latitude: number;
        longitude: number;
    }) => void;
    onTouch?: () => void;
    showsMyLocationButton?: boolean;
    compass?: boolean;
    scaleBar?: boolean;
    zoomControl?: boolean;
    mapType?: MapType;
    buildingHeight?: number;
    nightMode?: boolean;
}
/// component method

animateToTwoCoordinates: (c1: Coord, c2: Coord) => void;
animateToCoordinates: (coords: Coord[], bounds?: {
    top: number;
    bottom: number;
    left: number;
    right: number;
}) => void;
animateToRegion: (region: Region) => void;
setLocationTrackingMode: (mode: number) => void;
showsMyLocationButton: (show: boolean) => void;
handleOnCameraChange: (event: React.SyntheticEvent<{}, {
    latitude: number;
    longitude: number;
    zoom: number;
}>) => void;
handleOnMapClick: (event: React.SyntheticEvent<{}, {
    x: number;
    y: number;
    latitude: number;
    longitude: number;
}>) => void;
```

### `Marker`
```ts
interface MarkerProps {
    coordinate: Coord
    anchor?: { x: number, y: number }
    pinColor?: string
    rotation?: number
    flat?: boolean
    image?: ImageSourcePropType
    onClick?: () => void
}
```

### `Polyline`
```ts
interface PolylineProps {
    coordinates: Coord[]
    strokeWidth?: number
    strokeColor?: string
    onClick?: () => void
}
```

### `Path`
```ts
interface PathProps {
    coordinates: Coord[]
    width?: number
    color?: string
    outlineWidth?: number
    passedColor?: string
    outlineColor?: string
    passedOutlineColor?: string
    pattern?: ImageSourcePropType
    patternInterval?: number
    onClick?: () => void
}
```

### `Circle`
```ts
export interface CircleProps {
    coordinate: Coord[]
    radius?: number;
    color?: string;
    outlineWidth?: number;
    outlineColor?: string;
    zIndex?: number;
    onClick?: () => void
}
```

### `Polygon`
```ts
export interface PolygonProps {
    coordinate: Coord[]
    outlineWidth?: number;
    outlineColor?: string
    color?: string;
    holes?: Coord[][];
    onClick?: () => void
}
```