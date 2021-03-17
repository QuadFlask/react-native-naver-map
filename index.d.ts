import React, { Component } from 'react';
import { ImageSourcePropType, StyleProp, ViewStyle } from 'react-native';
declare const RNNaverMapView: any;
export interface Coord {
    latitude: number;
    longitude: number;
}
export interface Region extends Coord {
    latitudeDelta: number;
    longitudeDelta: number;
}
export declare enum TrackingMode {
    None = 0,
    NoFollow = 1,
    Follow = 2,
    Face = 3
}
export declare enum MapType {
    Basic = 0,
    Navi = 1,
    Satellite = 2,
    Hybrid = 3,
    Terrain = 4
}
export declare enum LayerGroup {
    LAYER_GROUP_BUILDING = "building",
    LAYER_GROUP_TRANSIT = "transit",
    LAYER_GROUP_BICYCLE = "bike",
    LAYER_GROUP_TRAFFIC = "ctt",
    LAYER_GROUP_CADASTRAL = "landparcel",
    LAYER_GROUP_MOUNTAIN = "mountain"
}
export declare enum Gravity {
    NO_GRAVITY = 0,
    AXIS_SPECIFIED = 1,
    AXIS_PULL_BEFORE = 2,
    AXIS_PULL_AFTER = 4,
    AXIS_X_SHIFT = 0,
    AXIS_Y_SHIFT = 4,
    TOP = 48,
    BOTTOM = 80,
    LEFT = 3,
    RIGHT = 5,
    CENTER_VERTICAL = 16,
    CENTER_HORIZONTAL = 1
}
export declare enum Align {
    Center = 0,
    Left = 1,
    Right = 2,
    Top = 3,
    Bottom = 4,
    TopLeft = 5,
    TopRight = 6,
    BottomRight = 7,
    BottomLeft = 8
}
export interface Rect {
    left?: number;
    top?: number;
    right?: number;
    bottom?: number;
}
export interface NaverMapViewProps {
    style?: StyleProp<ViewStyle>;
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
    minZoomLevel?: number;
    maxZoomLevel?: number;
    nightMode?: boolean;
    scrollGesturesEnabled?: boolean;
    zoomGesturesEnabled?: boolean;
    tiltGesturesEnabled?: boolean;
    rotateGesturesEnabled?: boolean;
    stopGesturesEnabled?: boolean;
    useTextureView?: boolean;
    locationTrackingMode?: number;
}
export default class NaverMapView extends Component<NaverMapViewProps> {
    ref?: RNNaverMapView;
    nodeHandle?: null | number;
    private resolveRef;
    animateToCoordinate: (coord: Coord) => void;
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
    private dispatchViewManagerCommand;
    handleOnCameraChange: (event: any) => any;
    handleOnMapClick: (event: any) => any;
    render(): any;
}
interface RNNaverMapView extends React.Component<{}, any> {
}
export interface MapOverlay {
    coordinate: Coord;
    onClick?: () => void;
}
export interface MarkerProps extends MapOverlay {
    anchor?: {
        x: number;
        y: number;
    };
    pinColor?: string;
    rotation?: number;
    flat?: boolean;
    image?: ImageSourcePropType;
    width?: number;
    height?: number;
    alpha?: number;
    animated?: boolean;
    caption?: {
        text?: string;
        align?: Align;
        textSize?: number;
        color?: string;
        haloColor?: string;
    };
    subCaption?: {
        text?: string;
        textSize?: number;
        color?: number;
        haloColor?: number;
    };
}
export declare class Marker extends Component<MarkerProps> {
    render(): any;
}
export interface CircleProps extends MapOverlay {
    radius?: number;
    color?: string;
    outlineWidth?: number;
    outlineColor?: string;
    zIndex?: number;
}
export declare class Circle extends Component<CircleProps> {
    render(): any;
}
interface PolylineProps extends Omit<MapOverlay, "coordinate"> {
    coordinates: Coord[];
    strokeWidth?: number;
    strokeColor?: string;
}
export declare class Polyline extends Component<PolylineProps> {
    render(): any;
}
interface PolygonProps extends Omit<MapOverlay, "coordinate"> {
    coordinates: Coord[];
    outlineWidth?: number;
    outlineColor?: string;
    color?: string;
    holes?: Coord[][];
}
export declare class Polygon extends Component<PolygonProps> {
    render(): any;
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
export declare class Path extends Component<PathProps> {
    render(): any;
}
export {};
