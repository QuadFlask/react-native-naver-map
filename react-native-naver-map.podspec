require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "react-native-naver-map"
  s.version      = package['version']
  s.summary      = "React Native Mapview component for iOS + Android"

  s.authors      = { "QuadFlask" => "pop9310@gmail.com" }
  s.homepage     = "https://github.com/quadflask/react-native-naver-map"
  s.license      = "MIT"
  s.platform     = :ios, "9.0"

  s.source       = { :git => "https://github.com/quadflask/react-native-naver-map.git" }
  s.source_files = "ios/navermap/**/*.{h,m}"

  s.dependency 'React'
  s.dependency 'NMapsMap', '~> 3.3.0'
  s.static_framework = true

end