import React, { Component } from 'react';
import { requireNativeComponent, findNodeHandle, UIManager, Platform, NativeModules, Image, } from 'react-native';
const RNNaverMapView = requireNativeComponent('RNNaverMapView');
const RNNaverMapMarker = requireNativeComponent('RNNaverMapMarker');
const RNNaverMapPathOverlay = requireNativeComponent('RNNaverMapPathOverlay');
const RNNaverMapPolylineOverlay = requireNativeComponent('RNNaverMapPolylineOverlay');
const RNNaverMapCircleOverlay = requireNativeComponent('RNNaverMapCircleOverlay');
const RNNaverMapPolygonOverlay = requireNativeComponent('RNNaverMapPolygonOverlay');
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
export const LayerGroup = {
    LAYER_GROUP_BUILDING: 'building',
    LAYER_GROUP_TRANSIT: 'transit',
    LAYER_GROUP_BICYCLE: 'bike',
    LAYER_GROUP_TRAFFIC: 'ctt',
    LAYER_GROUP_CADASTRAL: 'landparcel',
    LAYER_GROUP_MOUNTAIN: 'mountain',
};
export var Gravity;
(function (Gravity) {
    Gravity[Gravity["NO_GRAVITY"] = 0] = "NO_GRAVITY";
    Gravity[Gravity["AXIS_SPECIFIED"] = 1] = "AXIS_SPECIFIED";
    Gravity[Gravity["AXIS_PULL_BEFORE"] = 2] = "AXIS_PULL_BEFORE";
    Gravity[Gravity["AXIS_PULL_AFTER"] = 4] = "AXIS_PULL_AFTER";
    Gravity[Gravity["AXIS_X_SHIFT"] = 0] = "AXIS_X_SHIFT";
    Gravity[Gravity["AXIS_Y_SHIFT"] = 4] = "AXIS_Y_SHIFT";
    Gravity[Gravity["TOP"] = 48] = "TOP";
    Gravity[Gravity["BOTTOM"] = 80] = "BOTTOM";
    Gravity[Gravity["LEFT"] = 3] = "LEFT";
    Gravity[Gravity["RIGHT"] = 5] = "RIGHT";
    Gravity[Gravity["CENTER_VERTICAL"] = 16] = "CENTER_VERTICAL";
    Gravity[Gravity["CENTER_HORIZONTAL"] = 1] = "CENTER_HORIZONTAL";
})(Gravity || (Gravity = {}));
export default class NaverMapView extends Component {
    constructor() {
        super(...arguments);
        this.resolveRef = (ref) => {
            this.ref = ref;
            this.nodeHandle = findNodeHandle(ref);
        };
        this.animateToTwoCoordinates = (c1, c2) => {
            this.dispatchViewManagerCommand('animateToTwoCoordinates', [c1, c2]);
        };
        this.animateToCoordinates = (coords, bounds) => {
            this.dispatchViewManagerCommand("animateToCoordinates", [coords, bounds]);
        };
        this.watchCameraChange = () => {
            this.dispatchViewManagerCommand('watchCameraChange', []);
        };
        this.setLocationTrackingMode = (mode) => {
            this.dispatchViewManagerCommand('setLocationTrackingMode', [mode]);
        };
        this.showsMyLocationButton = (show) => {
            this.dispatchViewManagerCommand('showsMyLocationButton', [show]);
        };
        this.dispatchViewManagerCommand = (command, arg) => {
            return Platform.select({
                // @ts-ignore
                android: () => UIManager.dispatchViewManagerCommand(this.nodeHandle, 
                // @ts-ignore
                UIManager.getViewManagerConfig('RNNaverMapView').Commands[command], arg),
                ios: () => NativeModules[`RNNaverMapView`][command](this.nodeHandle, ...arg),
            })();
        };
        this.handleOnCameraChange = (event) => this.props.onCameraChange && this.props.onCameraChange(event.nativeEvent);
        this.handleOnMapClick = (event) => this.props.onMapClick && this.props.onMapClick(event.nativeEvent);
    }
    render() {
        const { onInitialized, center, tilt, bearing, mapPadding, logoMargin, nightMode, } = this.props;
        return React.createElement(RNNaverMapView, Object.assign({ ref: this.resolveRef }, this.props, { onInitialized: onInitialized, center: center, mapPadding: mapPadding, logoMargin: logoMargin, tilt: tilt, bearing: bearing, nightMode: nightMode, onCameraChange: this.handleOnCameraChange, onMapClick: this.handleOnMapClick }));
    }
}
export class Marker extends Component {
    render() {
        return React.createElement(RNNaverMapMarker, Object.assign({}, this.props, { image: getImageUri(this.props.image) }));
    }
}
export class Circle extends Component {
    render() {
        return React.createElement(RNNaverMapCircleOverlay, Object.assign({}, this.props));
    }
}
export class Polyline extends Component {
    render() {
        return React.createElement(RNNaverMapPolylineOverlay, Object.assign({}, this.props));
    }
}
export class Polygon extends Component {
    render() {
        return React.createElement(RNNaverMapPolygonOverlay, Object.assign({}, this.props));
    }
}
export class Path extends Component {
    render() {
        return React.createElement(RNNaverMapPathOverlay, Object.assign({}, this.props, { pattern: getImageUri(this.props.pattern) }));
    }
}
function getImageUri(src) {
    let imageUri = null;
    if (src) {
        let image = Image.resolveAssetSource(src) || { uri: null };
        imageUri = image.uri;
    }
    return imageUri;
}
