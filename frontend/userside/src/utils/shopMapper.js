/**
 * Backend ShopCardDTO / WishlistDTO expose gutTrustScore.
 * ShopDetailsDTO exposes finalGutTrustScore.
 */

export function getShopTrustScore(shop) {
  if (!shop) {
    return 0;
  }

  return (
    shop.finalGutTrustScore ??
    shop.gutTrustScore ??
    shop.trustScore ??
    0
  );
}
