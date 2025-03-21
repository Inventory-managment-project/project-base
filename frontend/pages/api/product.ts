// pages/api/products.ts
import { NextApiRequest, NextApiResponse } from "next";

export default function handler(req: NextApiRequest, res: NextApiResponse) {
  if (req.method === "POST") {
    const newProduct = req.body;
    // Aquí puedes validar y guardar el producto en la base de datos
    // Por ahora, devolvemos un mensaje de éxito para pruebas:
    return res.status(200).json({ message: "Producto creado correctamente", newProduct });
  } else {
    res.setHeader("Allow", ["POST"]);
    return res.status(405).end(`Method ${req.method} Not Allowed`);
  }
}
