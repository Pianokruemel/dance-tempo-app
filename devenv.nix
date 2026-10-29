{ pkgs, ... }:

{
  languages.java = {
    enable = true;
    jdk = pkgs.openjdk17;
    gradle.enable = true;
  };

  packages = [
    pkgs.android-tools
  ];

  enterShell = ''
    echo "💃 DanceTempo App devenv ready."
  '';
}
