import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

const API_AUTH_URL = "http://localhost:8080/validate";

export async function middleware(req: NextRequest) {
  const token = req.cookies.get("token")?.value;

  if (!token) {
    return NextResponse.redirect(new URL("/auth", req.url));
  }
  /*
  try {
    const response = await fetch(API_AUTH_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ token: token }),
      credentials: "include"
    });

    if (!response.ok) {
      return NextResponse.redirect(new URL("/auth", req.url));
    }
  } catch (error) {
    console.error("Error durante la validación del token:", error);
    return NextResponse.redirect(new URL("/auth", req.url));
  }
  */

  return NextResponse.next();
}

export const config = {
  matcher: ["/panel/:path*"]
};
