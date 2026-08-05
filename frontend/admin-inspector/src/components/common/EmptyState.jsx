<tbody>

    {shops.length === 0 ? (

        <tr>

            <td
                colSpan="5"
                className="text-center p-6 text-gray-500"
            >
                No shops found.
            </td>

        </tr>

    ) : (

        shops.map(shop => (

            <tr key={shop.shopId} className="border-b">

                <td className="p-3">{shop.shopName}</td>

                <td className="p-3">{shop.category}</td>

                <td className="p-3">{shop.finalGutTrustScore}</td>

                <td className="p-3">{shop.status}</td>

                <td className="p-3">
                    {shop.blocked ? "Yes" : "No"}
                </td>

            </tr>

        ))

    )}

</tbody>