package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.model.Building;
import com.example.myapplication.model.Point;
import com.example.myapplication.model.Room;

import java.util.List;

public class BuildingPlanView extends View {
    
    private Building building;
    private Paint roomPaint;
    private Paint patiosPaint;
    private Paint strokePaint;
    private Paint textPaint;
    private OnRoomClickListener roomClickListener;
    
    // Colores para diferentes tipos de ambientes
    private static final int SALON_COLOR = Color.parseColor("#E3F2FD");
    private static final int PATIO_COLOR = Color.parseColor("#E8F5E8");
    private static final int STROKE_COLOR = Color.parseColor("#424242");
    private static final int TEXT_COLOR = Color.parseColor("#212121");
    
    public interface OnRoomClickListener {
        void onRoomClick(Room room);
    }
    
    public BuildingPlanView(Context context) {
        super(context);
        init();
    }
    
    public BuildingPlanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public BuildingPlanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        // Configurar paint para salones
        roomPaint = new Paint();
        roomPaint.setAntiAlias(true);
        roomPaint.setStyle(Paint.Style.FILL);
        
        // Configurar paint para patios
        patiosPaint = new Paint();
        patiosPaint.setAntiAlias(true);
        patiosPaint.setStyle(Paint.Style.FILL);
        
        // Configurar paint para bordes
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(STROKE_COLOR);
        strokePaint.setStrokeWidth(3f);
        
        // Configurar paint para texto
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(36f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }
    
    public void setBuilding(Building building) {
        this.building = building;
        invalidate(); // Redibuja la vista
    }
    
    public void setOnRoomClickListener(OnRoomClickListener listener) {
        this.roomClickListener = listener;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (building == null || building.getRooms() == null) {
            return;
        }
        
        // Obtener dimensiones del canvas
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();
        
        // Calcular padding interno para mejor organización
        float paddingX = canvasWidth * 0.1f; // 10% de padding horizontal
        float paddingY = canvasHeight * 0.1f; // 10% de padding vertical
        
        // Área disponible para dibujar
        float availableWidth = canvasWidth - (2 * paddingX);
        float availableHeight = canvasHeight - (2 * paddingY);
        
        // Dibujar cada ambiente con mejor organización
        for (Room room : building.getRooms()) {
            drawRoomWithPadding(canvas, room, paddingX, paddingY, availableWidth, availableHeight);
        }
    }
    
    private void drawRoomWithPadding(Canvas canvas, Room room, float paddingX, float paddingY, 
                                   float availableWidth, float availableHeight) {
        List<Point> vertices = room.getVertices();
        if (vertices == null || vertices.size() < 3) {
            return;
        }
        
        // Crear el path del polígono con padding aplicado
        Path path = new Path();
        Point firstPoint = vertices.get(0);
        
        // Aplicar padding y escala a las coordenadas
        float scaledX = paddingX + (firstPoint.getX() * availableWidth / 1000f);
        float scaledY = paddingY + (firstPoint.getY() * availableHeight / 800f);
        path.moveTo(scaledX, scaledY);
        
        for (int i = 1; i < vertices.size(); i++) {
            Point point = vertices.get(i);
            scaledX = paddingX + (point.getX() * availableWidth / 1000f);
            scaledY = paddingY + (point.getY() * availableHeight / 800f);
            path.lineTo(scaledX, scaledY);
        }
        path.close();
        
        // Seleccionar color según el tipo de ambiente
        if ("patio".equals(room.getType())) {
            patiosPaint.setColor(PATIO_COLOR);
            canvas.drawPath(path, patiosPaint);
        } else {
            roomPaint.setColor(SALON_COLOR);
            canvas.drawPath(path, roomPaint);
        }
        
        // Dibujar el borde con mayor grosor para mejor visibilidad
        strokePaint.setStrokeWidth(3f);
        canvas.drawPath(path, strokePaint);
        
        // Dibujar la etiqueta en el centro con mejor posicionamiento
        Point center = room.getCenter();
        if (center != null) {
            float centerX = paddingX + (center.getX() * availableWidth / 1000f);
            float centerY = paddingY + (center.getY() * availableHeight / 800f);
            
            // Configurar texto con mejor legibilidad
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(32f);
            textPaint.setColor(TEXT_COLOR);
            
            // Dibujar el nombre del ambiente
            canvas.drawText(room.getName(), centerX, centerY - 8, textPaint);
            
            // Dibujar el tipo de ambiente con texto más pequeño
            Paint smallTextPaint = new Paint(textPaint);
            smallTextPaint.setTextSize(24f);
            smallTextPaint.setColor(Color.parseColor("#666666"));
            smallTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("(" + room.getType() + ")", centerX, centerY + 24, smallTextPaint);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && roomClickListener != null && building != null) {
            float x = event.getX();
            float y = event.getY();
            
            // Obtener dimensiones del canvas para calcular coordenadas escaladas
            int canvasWidth = getWidth();
            int canvasHeight = getHeight();
            float paddingX = canvasWidth * 0.1f;
            float paddingY = canvasHeight * 0.1f;
            float availableWidth = canvasWidth - (2 * paddingX);
            float availableHeight = canvasHeight - (2 * paddingY);
            
            // Convertir coordenadas de toque a coordenadas del plano
            float planX = (x - paddingX) * 1000f / availableWidth;
            float planY = (y - paddingY) * 800f / availableHeight;
            
            // Verificar si el toque está dentro de algún ambiente
            for (Room room : building.getRooms()) {
                if (isPointInRoom(planX, planY, room)) {
                    roomClickListener.onRoomClick(room);
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }
    
    private boolean isPointInRoom(float x, float y, Room room) {
        List<Point> vertices = room.getVertices();
        if (vertices == null || vertices.size() < 3) {
            return false;
        }
        
        // Algoritmo de ray casting para determinar si el punto está dentro del polígono
        boolean inside = false;
        int j = vertices.size() - 1;
        
        for (int i = 0; i < vertices.size(); i++) {
            Point pi = vertices.get(i);
            Point pj = vertices.get(j);
            
            if (((pi.getY() > y) != (pj.getY() > y)) &&
                (x < (pj.getX() - pi.getX()) * (y - pi.getY()) / (pj.getY() - pi.getY()) + pi.getX())) {
                inside = !inside;
            }
            j = i;
        }
        
        return inside;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Establecer un tamaño mínimo para la vista
        int minWidth = 1000;
        int minHeight = 800;
        
        int width = Math.max(minWidth, MeasureSpec.getSize(widthMeasureSpec));
        int height = Math.max(minHeight, MeasureSpec.getSize(heightMeasureSpec));
        
        setMeasuredDimension(width, height);
    }
}