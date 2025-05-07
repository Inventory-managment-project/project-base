import React from "react";
import { useNotifications } from "@/context/NotificationsContext";

interface NotificationBadgeProps {
  className?: string;
}

export const NotificationBadge: React.FC<NotificationBadgeProps> = ({ className = "" }) => {
  const { unreadCount } = useNotifications();

  if (unreadCount === 0) return null;

  return (
    <span className={`inline-flex items-center justify-center h-5 min-w-5 px-1 text-xs font-bold text-white bg-danger rounded-full ${className}`}>
      {unreadCount > 9 ? "9+" : unreadCount}
    </span>
  );
}; 