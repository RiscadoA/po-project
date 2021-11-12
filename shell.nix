# Shell environment for development on NixOS
let
  PROJECT_ROOT = builtins.getEnv "PWD";
in { pkgs ? import <nixpkgs> {} }: with pkgs; mkShell {
  nativeBuildInputs = [
    cvs
    openjdk16
  ];

  CLASSPATH = "${PROJECT_ROOT}/lib/po-uilib/po-uilib.jar:${PROJECT_ROOT}/ggc-core/ggc-core.jar:${PROJECT_ROOT}/ggc-app/ggc-app.jar";
}
