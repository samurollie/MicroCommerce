import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

export function middleware(request: NextRequest) {
/*   const token = request.cookies.get('token')
  const isAuthPage = request.nextUrl.pathname.startsWith('/login')
  const page = request.nextUrl.pathname

  if (!token && !isAuthPage) {
    return NextResponse.redirect(new URL(`/login?redirectTo=${page}`, request.url))
  }

  if (token && isAuthPage) {
    return NextResponse.redirect(new URL('/', request.url))
  } */

  return NextResponse.next()
}

export const config = {
  matcher: [
    '/cart/:path*',
    '/profile/:path*',
    '/orders/:path*',
    '/login'
  ]
}