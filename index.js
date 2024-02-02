import React, { Component } from 'react';
import { findNodeHandle, Image, NativeModules, Platform, processColor, requireNativeComponent, UIManager, } from 'react-native';
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
export var TrackingMode;
(function (TrackingMode) {
    TrackingMode[TrackingMode["None"] = 0] = "None";
    TrackingMode[TrackingMode["NoFollow"] = 1] = "NoFollow";
    TrackingMode[TrackingMode["Follow"] = 2] = "Follow";
    TrackingMode[TrackingMode["Face"] = 3] = "Face";
})(TrackingMode || (TrackingMode = {}));
export var MapType;
(function (MapType) {
    MapType[MapType["Basic"] = 0] = "Basic";
    MapType[MapType["Navi"] = 1] = "Navi";
    MapType[MapType["Satellite"] = 2] = "Satellite";
    MapType[MapType["Hybrid"] = 3] = "Hybrid";
    MapType[MapType["Terrain"] = 4] = "Terrain";
})(MapType || (MapType = {}));
export var LayerGroup;
(function (LayerGroup) {
    LayerGroup["LAYER_GROUP_BUILDING"] = "building";
    LayerGroup["LAYER_GROUP_TRANSIT"] = "transit";
    LayerGroup["LAYER_GROUP_BICYCLE"] = "bike";
    LayerGroup["LAYER_GROUP_TRAFFIC"] = "ctt";
    LayerGroup["LAYER_GROUP_CADASTRAL"] = "landparcel";
    LayerGroup["LAYER_GROUP_MOUNTAIN"] = "mountain";
})(LayerGroup || (LayerGroup = {}));
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
export var Align;
(function (Align) {
    Align[Align["Center"] = 0] = "Center";
    Align[Align["Left"] = 1] = "Left";
    Align[Align["Right"] = 2] = "Right";
    Align[Align["Top"] = 3] = "Top";
    Align[Align["Bottom"] = 4] = "Bottom";
    Align[Align["TopLeft"] = 5] = "TopLeft";
    Align[Align["TopRight"] = 6] = "TopRight";
    Align[Align["BottomRight"] = 7] = "BottomRight";
    Align[Align["BottomLeft"] = 8] = "BottomLeft";
})(Align || (Align = {}));
export default class NaverMapView extends Component {
    constructor() {
        super(...arguments);
        this.resolveRef = (ref) => {
            this.ref = ref;
            this.nodeHandle = findNodeHandle(ref);
        };
        this.animateToCoordinate = (coord, zoom) => {
            const zoomLevel = zoom ?? -1;
            this.dispatchViewManagerCommand('animateToCoordinate', [coord, zoomLevel]);
        };
        this.animateToTwoCoordinates = (c1, c2) => {
            this.dispatchViewManagerCommand('animateToTwoCoordinates', [c1, c2]);
        };
        this.animateToCoordinates = (coords, bounds) => {
            this.dispatchViewManagerCommand("animateToCoordinates", [coords, bounds]);
        };
        this.animateToRegion = (region) => {
            this.dispatchViewManagerCommand('animateToRegion', [region]);
        };
        this.setLocationTrackingMode = (mode) => {
            this.dispatchViewManagerCommand('setLocationTrackingMode', [mode]);
        };
        this.setLayerGroupEnabled = (group, enabled) => {
            this.dispatchViewManagerCommand('setLayerGroupEnabled', [group, enabled]);
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
        const { onInitialized, center, tilt, bearing, mapPadding, logoMargin, nightMode, useTextureView, } = this.props;
        const ViewClass = useTextureView ? RNNaverMapViewTexture : RNNaverMapView;
        return React.createElement(ViewClass, Object.assign({ ref: this.resolveRef }, this.props, { onInitialized: onInitialized, center: center, mapPadding: mapPadding, logoMargin: logoMargin, tilt: tilt, bearing: bearing, nightMode: nightMode, onCameraChange: this.handleOnCameraChange, onMapClick: this.handleOnMapClick }));
    }
}
export class Marker extends Component {
    render() {
        var _a, _b;
        return React.createElement(RNNaverMapMarker, Object.assign({}, this.props, { image: getImageUri(this.props.image), caption: this.props.caption && Object.assign(Object.assign({}, this.props.caption), { textSize: (_a = this.props.caption.textSize) !== null && _a !== void 0 ? _a : 12, color: parseColor(this.props.caption.color), haloColor: parseColor(this.props.caption.haloColor) }), subCaption: this.props.subCaption && Object.assign(Object.assign({}, this.props.subCaption), { textSize: (_b = this.props.subCaption.textSize) !== null && _b !== void 0 ? _b : 12, color: parseColor(this.props.subCaption.color), haloColor: parseColor(this.props.subCaption.haloColor) }) }));
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
        return Platform.select({
            android: () => React.createElement(RNNaverMapPolygonOverlay, Object.assign({}, this.props)),
            ios: () => React.createElement(RNNaverMapPolygonOverlay, Object.assign({}, this.props, { coordinates: {
                    exteriorRing: this.props.coordinates,
                    interiorRings: this.props.holes,
                } }))
        })();
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
function parseColor(color) {
    if (color && Platform.OS === 'ios')
        return processColor(color);
    return color;
}
