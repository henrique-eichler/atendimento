#!/bin/bash

clear

rm -rf ../resources/static/*
npm run build:debug

echo "Debug build completed. Source maps are enabled for browser debugging."