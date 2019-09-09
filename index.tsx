import React, {Component, SyntheticEvent} from 'react';
import {requireNativeComponent, findNodeHandle, UIManager, StyleProp, ViewStyle, Platform, NativeModules, ImageSourcePropType, Image} from 'react-native';

const RNNaverMapView = requireNativeComponent('RNNaverMapView');
const RNNaverMapMarker = requireNativeComponent('RNNaverMapMarker');
const RNNaverMapPathOverlay = requireNativeComponent('RNNaverMapPathOverlay');
const RNNaverMapPolylineOverlay = requireNativeComponent('RNNaverMapPolylineOverlay');

export interface Coord {
    latitude: number;
    longitude: number;
}

export const TrackingMode = {
    None: 0,
    NoFollow: 1,
    Follow: 2,
    Face: 3,
};

export const MapType = {
    Basic: 0,
    Navi: 1,
    Satellite: 2,
    Hybrid: 3,
    Terrain: 4,
};

export const CaptionAlign = {
    Center: 0,
    Left: 0,
    Right: 0,
    Top: 0,
    Bottom: 0,
    TopLeft: 0,
    TopRight: 0,
    BottomRight: 0,
    BottomLeft: 0,
};

export const LayerGroup = {
    LAYER_GROUP_BUILDING: "building",
    LAYER_GROUP_TRANSIT: "transit",
    LAYER_GROUP_BICYCLE: "bike",
    LAYER_GROUP_TRAFFIC: "ctt",
    LAYER_GROUP_CADASTRAL: "landparcel",
    LAYER_GROUP_MOUNTAIN: "mountain",
};

export interface NaverMapViewProps {
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
    onMapClick?: (event: {
        x: number,
        y: number,
        latitude: number,
        longitude: number,
    }) => void,
    onTouch?: () => void,
    showsMyLocationButton?: boolean,
    compass?: boolean,
    scaleBar?: boolean,
    zoomControl?: boolean,
    mapType?: number,
    buildingHeight?: number,
    nightMode?: boolean,
}

export default class NaverMapView extends Component<NaverMapViewProps> {
    ref?: RNNaverMapView;
    nodeHandle?: null | number;

    private resolveRef = (ref: RNNaverMapView) => {
        this.ref = ref;
        this.nodeHandle = findNodeHandle(ref)
    };

    animateToTwoCoordinates = (c1: Coord, c2: Coord) => {
        this.dispatchViewManagerCommand("animateToTwoCoordinates", [c1, c2]);
    };

    watchCameraChange = () => {
        this.dispatchViewManagerCommand("watchCameraChange", []);
    };

    setLocationTrackingMode = (mode: number) => {
        this.dispatchViewManagerCommand("setLocationTrackingMode", [mode]);
    };

    showsMyLocationButton = (show: boolean) => {
        this.dispatchViewManagerCommand("showsMyLocationButton", [show]);
    };

    private dispatchViewManagerCommand = (command: string, arg: any) => {
        return Platform.select({
            // @ts-ignore
            android: () => UIManager.dispatchViewManagerCommand(
                this.nodeHandle,
                // @ts-ignore
                UIManager.getViewManagerConfig("RNNaverMapView").Commands[command],
                arg,
            ),
            ios: () => NativeModules[`RNNaverMapView`][command](this.nodeHandle, ...arg)
        })();
    };

    handleOnCameraChange = (event: SyntheticEvent<{}, {
        latitude: number,
        longitude: number,
        zoom: number,
    }>) => this.props.onCameraChange && this.props.onCameraChange(event.nativeEvent);

    handleOnMapClick = (event: SyntheticEvent<{}, {
        x: number,
        y: number,
        latitude: number,
        longitude: number,
    }>) => {
        this.props.onMapClick && this.props.onMapClick(event.nativeEvent);
    };

    render() {
        const {
            onInitialized,
            center,
            tilt,
            bearing,
            mapPadding,
            nightMode
        } = this.props;

        return <RNNaverMapView ref={this.resolveRef}
        {...this.props}
        onInitialized={onInitialized}
        center={center}
        mapPadding={mapPadding}
        tilt={tilt}
        bearing={bearing}
        nightMode={nightMode}
                               onCameraChange={this.handleOnCameraChange}
                               onMapClick={this.handleOnMapClick}
        />
    }
}

interface RNNaverMapView extends React.Component<{}, any> {
}

interface MarkerProps {
    coordinate: Coord
    anchor?: { x: number, y: number }
    pinColor?: string
    rotation?: number
    flat?: boolean
    image?: ImageSourcePropType
    width?: number
    height?: number
    caption?: {
        text?: string
        align?: number
        textSize?: number
        color?: number
        haloColor?: number
    }
    subCaption?: {
        text?: string
        textSize?: number
        color?: number
        haloColor?: number
    }
}

export class Marker extends Component<MarkerProps> {
    render() {
        return <RNNaverMapMarker {...this.props} image={getImageUri(this.props.image)}/>
    }
}

interface PolylineProps {
    coordinates: Coord[]
    strokeWidth?: number
    strokeColor?: string
}

export class Polyline extends Component<PolylineProps> {
    render() {
        return <RNNaverMapPolylineOverlay {...this.props}/>
    }
}

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

export class Path extends Component<PathProps> {
    render() {
        return <RNNaverMapPathOverlay {...this.props} pattern={getImageUri(this.props.pattern)}/>
    }
}

function getImageUri(src?: ImageSourcePropType): string | null {
    let imageUri = null;
    if (src) {
        let image = Image.resolveAssetSource(src) || {uri: null};
        imageUri = image.uri;
    }
    return imageUri;
}
