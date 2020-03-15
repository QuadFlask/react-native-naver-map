import React, { Component } from 'react';
import { StyleProp, ViewStyle, ImageSourcePropType } from 'react-native';
declare const RNNaverMapView: any;
export interface Coord {
    latitude: number;
    longitude: number;
}
export declare const TrackingMode: {
    None: number;
    NoFollow: number;
    Follow: number;
    Face: number;
};
export declare const MapType: {
    Basic: number;
    Navi: number;
    Satellite: number;
    Hybrid: number;
    Terrain: number;
};
export declare const LayerGroup: {
    LAYER_GROUP_BUILDING: string;
    LAYER_GROUP_TRANSIT: string;
    LAYER_GROUP_BICYCLE: string;
    LAYER_GROUP_TRAFFIC: string;
    LAYER_GROUP_CADASTRAL: string;
    LAYER_GROUP_MOUNTAIN: string;
};
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
    mapType?: number;
    buildingHeight?: number;
    nightMode?: boolean;
}
export default class NaverMapView extends Component<NaverMapViewProps> {
    ref?: RNNaverMapView;
    nodeHandle?: null | number;
    private resolveRef;
    animateToTwoCoordinates: (c1: Coord, c2: Coord) => void;
    animateToCoordinates: (coords: Coord[], bounds?: {
        top: number;
        bottom: number;
        left: number;
        right: number;
    }) => void;
    watchCameraChange: () => void;
    setLocationTrackingMode: (mode: number) => void;
    showsMyLocationButton: (show: boolean) => void;
    private dispatchViewManagerCommand;
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
    render(): JSX.Element;
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
        align?: number;
        textSize?: number;
        color?: number;
        haloColor?: number;
    };
    subCaption?: {
        text?: string;
        textSize?: number;
        color?: number;
        haloColor?: number;
    };
}
export declare class Marker extends Component<MarkerProps> {
    render(): JSX.Element;
}
export interface CircleProps extends MapOverlay {
    radius?: number;
    color?: string;
    outlineWidth?: number;
    outlineColor?: string;
    zIndex?: number;
}
export declare class Circle extends Component<CircleProps> {
    render(): JSX.Element;
}
interface PolylineProps extends Omit<MapOverlay, "coordinate"> {
    coordinates: Coord[];
    strokeWidth?: number;
    strokeColor?: string;
}
export declare class Polyline extends Component<PolylineProps> {
    render(): JSX.Element;
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
    render(): JSX.Element;
}
export {};
