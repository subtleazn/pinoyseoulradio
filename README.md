# PinoySeoul Radio: Open-Source Android Streaming App

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/pinoyseoul/pinoyseoulradio)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

> Bringing Filipino radio to the world, one stream at a time.

PinoySeoul Radio is an open-source, single-stream radio streaming application meticulously developed for PinoySeoul Media Enterprise. This project serves as a robust and customizable solution for delivering high-quality audio content to a global audience via Android devices. Designed with performance and user experience in mind, it offers a seamless listening experience and a flexible architecture for easy branding and content management.

## Why PinoySeoul Radio?

In a world of complex, multi-featured music apps, PinoySeoul Radio focuses on one thing: providing a simple, reliable, and high-quality listening experience for a single radio stream. This project was born out of a desire to create a lightweight, open-source, and easily customizable Android app for independent broadcasters and media enterprises.

## Features

*   **Single-Stream Playback:** Optimized for continuous, high-fidelity audio streaming.
*   **Customizable Brand Identity:** Easily adapt app name, icon, splash screen, and color scheme.
*   **Dynamic Stream Configuration:** Effortlessly update radio stream URLs and quality options.
*   **Firebase Integration:** Support for OneSignal (push notifications), AdMob (monetization), and Google Services.
*   **User-Friendly Interface:** Clean and intuitive design for an engaging listening experience.
*   **Background Playback:** Continue listening to your favorite station while using other apps.
*   **Media Controls:** Control playback from the lock screen and notification shade.
*   **Robust Build Process:** Streamlined APK and App Bundle generation for Google Play Store deployment.

## Architecture Overview

The PinoySeoul Radio app follows a standard Android architecture, with a focus on separation of concerns and modularity.

*   **UI Layer:** Activities and Fragments built with View Binding.
*   **Service Layer:** A `RadioService` manages background playback and media notifications.
*   **Player Engine:** `PlayerManager` utilizes ExoPlayer for robust and efficient audio streaming.
*   **Backend Integration:** Firebase and OneSignal are integrated for ads, analytics, and push notifications.

## Getting Started

To get a local copy of PinoySeoul Radio up and running on your development environment, follow the detailed instructions in our [Setup Guide](docs/SETUP.md).

## Customization

Tailor the PinoySeoul Radio app to your specific brand and streaming needs. Our [Customization Checklist](docs/CUSTOMIZATION.md) provides a step-by-step guide.

## Deployment

Ready to publish your customized PinoySeoul Radio app? Our [Deployment Guide](docs/DEPLOYMENT.md) walks you through the process.

## Contributing

We welcome contributions to the PinoySeoul Radio project! If you have suggestions for improvements, bug fixes, or new features, please see our [Contributing Guide](CONTRIBUTING.md).

## Code of Conduct

To ensure a welcoming and inclusive community, we have a [Code of Conduct](CODE_OF_CONDUCT.md) that all contributors are expected to follow.

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

Project Link: [https://github.com/pinoyseoul/pinoyseoulradio](https://github.com/pinoyseoul/pinoyseoulradio)
Support Email: [support@pinoyseoul.com](mailto:support@pinoyseoul.com)

## Acknowledgements

*   PinoySeoul Media Enterprise
*   Open-source community