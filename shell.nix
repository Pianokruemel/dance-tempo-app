{ pkgs ? import <nixpkgs> {
    config = {
      allowUnfree = true;
      android_sdk.accept_license = true;
    };
  }
}:

let
  androidComposition = pkgs.androidenv.composeAndroidPackages {
    cmdLineToolsVersion = "13.0";
    platformToolsVersion = "35.0.2";
    buildToolsVersions = [ "35.0.0" ];
    platformVersions = [ "35" ];
    abiVersions = [ "x86_64" ];
    includeEmulator = false;
    includeSources = false;
    includeSystemImages = false;
  };
in
pkgs.mkShell {
  name = "dance-tempo-android-env";

  buildInputs = with pkgs; [
    openjdk17
    gradle
    androidComposition.androidsdk
    which
  ];

  shellHook = ''
    export JAVA_HOME=${pkgs.openjdk17}
    export ANDROID_HOME=${androidComposition.androidsdk}/libexec/android-sdk
    export ANDROID_SDK_ROOT=$ANDROID_HOME
    export PATH="$JAVA_HOME/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin:$PATH"
    echo "=========================================================="
    echo "💃 DanceTempo Android Dev Environment (Nix)"
    echo "Java:        $(java -version 2>&1 | head -n 1)"
    echo "Android SDK: $ANDROID_HOME"
    echo "=========================================================="
  '';
}
