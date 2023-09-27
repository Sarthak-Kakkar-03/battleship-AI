# Change Log for PA04
## ArtIntel log
- Reimplemented TakeShots method and added some fields to keep a  track of what coordinates have already been hit and what remaining coordinates we are allowed to hit.
- Since the previous implementation falsely assumed that hitting a coordinate twice would just result in the normal hit status and nothing happens.
- Although just to be clear, neither implementations had repeated shots in the same volley.

## ProxyController
- Created the proxy controller from scratch of course.
 ## Controller
- Added a method called simulate war just for my own testing, it makes an AI fight against another AI and I will be deleting/commenting before submission