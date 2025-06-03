# Frontend Debugging Guide

This document explains how to build and debug the Vue.js frontend application.

## Build Modes

The application supports two build modes:

### Production Build

```bash
./build.sh
```

This builds the application for production with optimized and minified code.

### Debug Build

```bash
./build-debug.sh
```

This builds the application with source maps enabled and without minification, making it easier to debug in the browser.

## Debugging in the Browser

When you build the application using the debug mode, you can use the browser's developer tools to debug the application:

1. Open your application in the browser
2. Open the browser's developer tools (F12 or Ctrl+Shift+I in most browsers)
3. Go to the "Sources" tab
4. You should be able to see your original Vue source files in the file explorer
5. Set breakpoints, inspect variables, and debug as needed

## Configuration Details

The debug build mode is configured in:

- `vite.config.js` - Configures source maps and disables minification in debug mode
- `package.json` - Contains the `build:debug` script that runs Vite with the debug mode
- `build-debug.sh` - Shell script to easily build the application in debug mode

## Tips for Effective Debugging

- Use `console.log()` statements to output values during execution
- Set breakpoints at critical points in your code
- Use the Vue Devtools browser extension for Vue-specific debugging
- Check the browser console for errors and warnings