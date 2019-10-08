react-native-naver-map
-----
네이버맵의 리액트 네이티브 브릿지입니다.

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
$ react-native link @react-native-community/async-storage
$ cd ios/ && pod install
```


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

[네이버 맵 IOS SDK 문서](https://navermaps.github.io/ios-map-sdk/guide-ko/1.html)를 따라 API키와 레포지터리 경로를 추가합니다

```
cd ios && pod install
```
`info.plist`에 아래와 같이 추가하고 발급받은 클라이언트 아이디로 바꿔줍니다.
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

## 컴포넌트

타입스크립트 타입 정의가 포함되어 있어 타입스크립트 사용을 추천합니다.

### `NaverMapView`
```ts
interface NaverMapViewProps {
    style?: StyleProp<ViewStyle>,
    center?: Coord & { zoom?: number, tilt?: number, bearing?: number },
    tilt?: number,
    bearing?: number,
    mapPadding?: { left: number, top: number, right: number, bottom: number },
    onInitialized?: Function,
    onCameraChange?: (event: {
        latitude: number,
        longitude: number,
        zoom: number,
    }) => void,
    showsMyLocationButton?: boolean,
    compass?: boolean,
    scaleBar?: boolean,
    zoomControl?: boolean,
    mapType?: number,
    buildingHeight?: number,
    nightMode?: boolean,
}
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
}
```

### `Polyline`
```ts
interface PolylineProps {
    coordinates: Coord[]
    strokeWidth?: number
    strokeColor?: string
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
}
```
