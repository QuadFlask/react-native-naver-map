import React, {Component, SyntheticEvent} from 'react';
import {findNodeHandle, Image, ImageSourcePropType, NativeModules, Platform, processColor, requireNativeComponent, StyleProp, UIManager, ViewStyle,} from 'react-native';

const RNNaverMapView = requireNativeComponent('RNNaverMapView');
const RNNaverMapViewTexture = Platform.select({
    android: () => requireNativeComponent('RNNaverMapViewTexture'),
    ios: () => RNNaverMapView
})();
const RNNaverMapMarker = requireNativeComponent('RNNaverMapMarker');
const RNNaverMapPathOverlay = requireNativeComponent('RNNaverMapPathOverlay');
const RNNaverMapPolylineOverlay = requireNativeComponent('RNNaverMapPolylineOverlay');
const RNNaverMapCircleOverlay = requireNativeComponent('RNNaverMapCircleOverlay');
const RNNaverMapPolygonOverlay = requireNativeComponent('RNNaverMapPolygonOverlay');

export interface Coord {
    latitude: number;
    longitude: number;
}

export interface Region extends Coord {
    latitudeDelta: number;
    longitudeDelta: number;
}

export enum TrackingMode {
    None = 0,
    NoFollow = 1,
    Follow = 2,
    Face = 3,
}

export enum MapType {
    Basic = 0,
    Navi = 1,
    Satellite = 2,
    Hybrid = 3,
    Terrain = 4,
}

export enum LayerGroup {
    LAYER_GROUP_BUILDING = 'building',
    LAYER_GROUP_TRANSIT = 'transit',
    LAYER_GROUP_BICYCLE = 'bike',
    LAYER_GROUP_TRAFFIC = 'ctt',
    LAYER_GROUP_CADASTRAL = 'landparcel',
    LAYER_GROUP_MOUNTAIN = 'mountain',
}

export enum Gravity {
    NO_GRAVITY = 0x0000,
    AXIS_SPECIFIED = 0x0001,
    AXIS_PULL_BEFORE = 0x0002,
    AXIS_PULL_AFTER = 0x0004,
    AXIS_X_SHIFT = 0,
    AXIS_Y_SHIFT = 4,
    TOP = (AXIS_PULL_BEFORE | AXIS_SPECIFIED) << AXIS_Y_SHIFT,
    BOTTOM = (AXIS_PULL_AFTER | AXIS_SPECIFIED) << AXIS_Y_SHIFT,
    LEFT = (AXIS_PULL_BEFORE | AXIS_SPECIFIED) << AXIS_X_SHIFT,
    RIGHT = (AXIS_PULL_AFTER | AXIS_SPECIFIED) << AXIS_X_SHIFT,
    CENTER_VERTICAL = AXIS_SPECIFIED << AXIS_Y_SHIFT,
    CENTER_HORIZONTAL = AXIS_SPECIFIED << AXIS_X_SHIFT,
}

export enum Align {
    Center,
    Left,
    Right,
    Top,
    Bottom,
    TopLeft,
    TopRight,
    BottomRight,
    BottomLeft,
}

export interface Rect {
    left?: number;
    top?: number;
    right?: number;
    bottom?: number;
}

export interface NaverMapViewProps {
    style?: StyleProp<ViewStyle>;
    center?: Coord & { zoom?: number; tilt?: number; bearing?: number };
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
        contentsRegion: [Coord, Coord, Coord, Coord, Coord];
        coveringRegion: [Coord, Coord, Coord, Coord, Coord];
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
    minZoomLevel?: number;
    maxZoomLevel?: number;
    nightMode?: boolean;
    scrollGesturesEnabled?: boolean;
    zoomGesturesEnabled?: boolean;
    tiltGesturesEnabled?: boolean;
    rotateGesturesEnabled?: boolean;
    stopGesturesEnabled?: boolean;
    liteModeEnabled?: boolean;
    useTextureView?: boolean;
    children?: Element;
}

export default class NaverMapView extends Component<NaverMapViewProps, {}> {
    ref?: RNNaverMapView;
    nodeHandle?: null | number;

    private resolveRef = (ref: RNNaverMapView) => {
        this.ref = ref;
        this.nodeHandle = findNodeHandle(ref);
    };

    animateToCoordinate = (coord: Coord) => {
        this.dispatchViewManagerCommand('animateToCoordinate', [coord]);
    }

    animateToTwoCoordinates = (c1: Coord, c2: Coord) => {
        this.dispatchViewManagerCommand('animateToTwoCoordinates', [c1, c2]);
    };

    animateToCoordinates = (coords: Coord[], bounds?: { top: number, bottom: number, left: number, right: number, }) => {
        this.dispatchViewManagerCommand("animateToCoordinates", [coords, bounds]);
    };

    animateToRegion = (region: Region) => {
        this.dispatchViewManagerCommand('animateToRegion', [region]);
    }

    setLocationTrackingMode = (mode: number) => {
        this.dispatchViewManagerCommand('setLocationTrackingMode', [mode]);
    };

    setLayerGroupEnabled = (group: LayerGroup, enabled: boolean) => {
        this.dispatchViewManagerCommand('setLayerGroupEnabled', [group, enabled]);
    };

    showsMyLocationButton = (show: boolean) => {
        this.dispatchViewManagerCommand('showsMyLocationButton', [show]);
    };

    private dispatchViewManagerCommand = (command: string, arg: any) => {
        return Platform.select({
            // @ts-ignore
            android: () => UIManager.dispatchViewManagerCommand(
                this.nodeHandle,
                // @ts-ignore
                UIManager.getViewManagerConfig('RNNaverMapView').Commands[command],
                arg,
            ),
            ios: () =>
                NativeModules[`RNNaverMapView`][command](this.nodeHandle, ...arg),
        })();
    };

    handleOnCameraChange = (event: SyntheticEvent<{}, {
        latitude: number;
        longitude: number;
        zoom: number;
        contentsRegion: [Coord, Coord, Coord, Coord, Coord];
        coveringRegion: [Coord, Coord, Coord, Coord, Coord];
    }>) => this.props.onCameraChange && this.props.onCameraChange(event.nativeEvent);

    handleOnMapClick = (event: SyntheticEvent<{}, {
        x: number;
        y: number;
        latitude: number;
        longitude: number;
    }>) => this.props.onMapClick && this.props.onMapClick(event.nativeEvent);

    render() {
        const {
            onInitialized,
            center,
            tilt,
            bearing,
            mapPadding,
            logoMargin,
            nightMode,
            useTextureView,
        } = this.props;

        const ViewClass = useTextureView ? RNNaverMapViewTexture : RNNaverMapView;

        return <ViewClass
            ref={this.resolveRef}
            {...this.props}
            onInitialized={onInitialized}
            center={center}
            mapPadding={mapPadding}
            logoMargin={logoMargin}
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

export interface MapOverlay {
    coordinate: Coord;
    onClick?: () => void;
}

export interface MarkerProps extends MapOverlay {
    anchor?: { x: number; y: number };
    pinColor?: string;
    rotation?: number;
    flat?: boolean;
    image?: ImageSourcePropType;
    width?: number;
    height?: number;
    alpha?: number;
    angle?: number;
    hidden?: boolean;
    zIndex?: number;
    iconPerspectiveEnabled?: boolean;
    isHideCollidedSymbols?: boolean;
    isHideCollidedMarkers?: boolean;
    isHideCollidedCaptions?: boolean;
    isForceShowIcon?: boolean;
    animated?: boolean;
    caption?: {
        text?: string;
        align?: Align;
        textSize?: number;
        color?: string;
        haloColor?: string;
        offset?: number;
        requestedWidth?: number;
        minZoom?: number;
        maxZoom?: number;
    };
    subCaption?: {
        text?: string;
        textSize?: number;
        color?: string;
        haloColor?: string;
        requestedWidth?: number;
        minZoom?: number;
        maxZoom?: number;
    };
}

export class Marker extends Component<MarkerProps> {
    render() {
        return <RNNaverMapMarker
            {...this.props}
            image={getImageUri(this.props.image)}
            caption={this.props.caption && {
                ...this.props.caption,
                textSize: this.props.caption.textSize ?? 12,
                color: parseColor(this.props.caption.color),
                haloColor: parseColor(this.props.caption.haloColor),
            }}
            subCaption={this.props.subCaption && {
                ...this.props.subCaption,
                textSize: this.props.subCaption.textSize ?? 12,
                color: parseColor(this.props.subCaption.color),
                haloColor: parseColor(this.props.subCaption.haloColor),
            }}/>
    }
}

export interface CircleProps extends MapOverlay {
    radius?: number;
    color?: string;
    outlineWidth?: number;
    outlineColor?: string;
    zIndex?: number;
}

export class Circle extends Component<CircleProps> {
    render() {
        return <RNNaverMapCircleOverlay {...this.props} />;
    }
}

interface PolylineProps extends Omit<MapOverlay, "coordinate"> {
    coordinates: Coord[];
    strokeWidth?: number;
    strokeColor?: string;
}

export class Polyline extends Component<PolylineProps> {
    render() {
        return <RNNaverMapPolylineOverlay {...this.props} />;
    }
}

interface PolygonProps extends Omit<MapOverlay, "coordinate"> {
    coordinates: Coord[];
    outlineWidth?: number;
    outlineColor?: string
    color?: string;
    holes?: Coord[][];
}

export class Polygon extends Component<PolygonProps> {
    render() {
        return Platform.select({
            android: () => <RNNaverMapPolygonOverlay {...this.props} />,
            ios: () => <RNNaverMapPolygonOverlay
                {...this.props}
                coordinates={{
                    exteriorRing: this.props.coordinates,
                    interiorRings: this.props.holes,
                }}
            />
        })();
    }
}

export interface PathProps extends Omit<MapOverlay, "coordinate"> {
    coordinates: Coord[];
    width?: number;
    color?: string;
    outlineWidth?: number;
    passedColor?: string;
    outlineColor?: string;
    passedOutlineColor?: string;
    pattern?: ImageSourcePropType;
    patternInterval?: number;
    progress?: number;
    zIndex?: number;
}

export class Path extends Component<PathProps> {
    render() {
        return <RNNaverMapPathOverlay
            {...this.props}
            pattern={getImageUri(this.props.pattern)}
        />
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

function parseColor(color?: string | null): string | null | undefined | number {
    if (color && Platform.OS === 'ios')
        return processColor(color);
    return color;
}
